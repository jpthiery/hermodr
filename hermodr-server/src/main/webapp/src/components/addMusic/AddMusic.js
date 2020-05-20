import React, {useCallback} from "react";
import {Box} from "@material-ui/core";
import Select from '@material-ui/core/Select';
import MenuItem from "@material-ui/core/MenuItem";

import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faYoutube} from "@fortawesome/free-brands-svg-icons";
import {faCloudDownloadAlt, faDownload} from "@fortawesome/free-solid-svg-icons";
import CardContent from "@material-ui/core/CardContent";
import CardActionArea from "@material-ui/core/CardActionArea";
import Button from "@material-ui/core/Button";
import Card from "@material-ui/core/Card";
import TextField from "@material-ui/core/TextField";
import {useDropzone} from 'react-dropzone'
import RootRef from '@material-ui/core/RootRef'
import {musicSubmitted, radioSelected} from "../../redux/selectors";
import {connect} from "react-redux";
import {submitMusicToServer} from "./actions";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";

export const AddMusic = ({defaultSelectedSource, radioId, musicsSubmittedToServer, submitMusicToServer}) => {

    const [disableAdd, setDisableAdd] = React.useState(true)
    const [selectedSource, setSelectedSource] = React.useState(defaultSelectedSource)
    const [inputFieldValue, setInputFieldValue] = React.useState("")

    const onDrop = useCallback((acceptedFiles) => {
        acceptedFiles.forEach((file) => {
            console.info(file)

            submitMusicToServer(radioId, {
                source: "LOCALFILE",
                file: file
            })

        })

    }, [])
    const {getRootProps, getInputProps} = useDropzone({onDrop})

    const {ref, ...rootProps} = getRootProps()
    const sourceAvailable = [
        {
            name: "local",
            icon: faDownload
        },
        {
            name: "youtube",
            icon: faYoutube
        }
    ]

    if (musicsSubmittedToServer.hasOwnProperty(inputFieldValue)) {
        setInputFieldValue("")
        setDisableAdd(true)
    }

    const handleSourceChange = (event) => {
        setSelectedSource(event.target.value)
    }

    const handleReferenceChange = (event) => {
        let reference = event.target.value
        switch (selectedSource) {
            case "youtube":
                reference = extractYoutubeReference(reference)
                setDisableAdd(!/^[0-9A-Za-z_-]{11,}$/.test(reference))
                break
        }
        setInputFieldValue(event.target.value)
    }
    const handleAddMusic = (event) => {
        submitMusicToServer(radioId, {
            source: "YOUTUBE",
            reference: extractYoutubeReference(inputFieldValue)
        })
    }

    const extractYoutubeReference = (inputValue) => {
        try {
            const url = new URL(inputValue)
            if (url.host.startsWith("www.youtube")) {
                return url.searchParams.get("v")
            }
        } catch (e) {

        }
        return inputFieldValue
    }

    const defineReferenceComponent = () => {
        switch (selectedSource) {
            case "youtube":
                return <Box flexGrow={1}>
                    <TextField
                        name="youtube_location"
                        variant="outlined"
                        size="small"
                        fullWidth={true}
                        label="Youtube url video"
                        value={inputFieldValue}
                        onChange={(e) => handleReferenceChange(e)}/>
                </Box>
            case "local":
                return <Box flexGrow={1}>
                    <RootRef rootRef={ref}>
                        <Paper {...rootProps} elevation={3}>
                            <Box display="flex" justifyContent="center" alignItem="center">
                                <Box style={{flex: "0 1 120px"}} justifyContent="center" alignItem="center">
                                    <input {...getInputProps()} />
                                    <FontAwesomeIcon icon={faCloudDownloadAlt} size={"3x"} style={{margin: "auto"}}/>
                                    <Typography component="h2" style={{width: "max-content"}}>
                                        Drag 'n' drop some musics files you want to share here.
                                    </Typography>
                                </Box>
                            </Box>
                        </Paper>
                    </RootRef>
                </Box>
        }
    }

    return (
        <Card>
            <CardContent>
                <Box display="flex" flexDirection="row">
                    <Box style={{marginRight: 10}}>
                        <Select defaultValue={defaultSelectedSource} onChange={e => handleSourceChange(e)}>
                            {sourceAvailable.map(value => {
                                return (
                                    <MenuItem value={value.name}>
                                        {value.name.charAt(0).toUpperCase() + value.name.slice(1)}
                                        <FontAwesomeIcon style={{marginLeft: 10}} icon={value.icon}/>
                                    </MenuItem>
                                )
                            })}
                        </Select>
                    </Box>
                    {defineReferenceComponent()}
                </Box>
            </CardContent>
            <CardActionArea>
                <Button
                    disabled={disableAdd}
                    onClick={(event) => handleAddMusic(event)}
                >
                    Add to playlist
                </Button>
            </CardActionArea>
        </Card>

    )
}


const mapStateToProps = state => {
    const radioId = radioSelected(state);
    const musicsSubmittedToServer = musicSubmitted(state)
    const defaultSelectedSource = "youtube"
    return {defaultSelectedSource, radioId, musicsSubmittedToServer};
};

export default connect(
    mapStateToProps,
    {submitMusicToServer}
)(AddMusic)