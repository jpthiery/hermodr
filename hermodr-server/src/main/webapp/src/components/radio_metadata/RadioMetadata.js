import React from "react";
import CardContent from "@material-ui/core/CardContent";
import Link from "@material-ui/core/Link";
import Typography from "@material-ui/core/Typography";
import Card from "@material-ui/core/Card";
import Button from "@material-ui/core/Button";

import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {faGithub} from "@fortawesome/free-brands-svg-icons/faGithub";

const RadioMetadata = ({radio}) => {
  const {name, urlToListen} = radio
  return (
    <Card elevation={1}>
      <CardContent>
        <Typography variant="h3" component="h5">
          {name}
        </Typography>
        <Button variant="outlined" size="large" color={"primary"} href={urlToListen} target="_blank">Listen</Button>
      </CardContent>
      <CardContent>
        <Typography variant="body2">
          Hermodr is an opensource share web radio dev for fun only.
        </Typography>
        <Typography variant="overline">
          Enjoy source code on
          <Link href="http://github.com/jpthiery/hermodr" target="_blank">
            <FontAwesomeIcon icon={faGithub} color="black"/>
          </Link>
        </Typography>
      </CardContent>
    </Card>
  )
}

export default RadioMetadata