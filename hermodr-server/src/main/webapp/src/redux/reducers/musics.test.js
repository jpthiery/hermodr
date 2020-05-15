import reducer from "./musics"
import {responseListRadio, responseRadioDetail, startMusic, validateMusic} from "./../actions"

describe('music reducer', () => {
  it('should return initial state', () => {
    expect(reducer(undefined, {})).toEqual({
      radios: {},
      radioSelected: "",
      musicsSubmitted: {}
    })
  })
  it('should add first radio', () => {
    expect(reducer(
      undefined,
      responseListRadio([{"id": "A", "name": "Awersome radio"}])
      )
    ).toEqual({
      radios: {
        "A": {
          "id": "A",
          "name": "Awersome radio"
        }
      },
      radioSelected: "A",
      musicsSubmitted: {}
    })
  });
  it('should add details about a radio', () => {
    expect(
      reducer(
        {
          radios: {
            "A": {
              "id": "A",
              "name": "Awersome radio"
            }
          },
          radioSelected: "A",
          musicsSubmitted: {}
        },
        responseRadioDetail({
          "id": "A",
          "name": "Awersome radio",
          "urlToListen": "http://radio.fake",
          "playlist": []
        })
      )
    )
      .toEqual(
        {
          radios: {
            "A": {
              "id": "A",
              "name": "Awersome radio",
              "urlToListen": "http://radio.fake",
              "playlist": []
            }
          },
          radioSelected: "A",
          musicsSubmitted: {}
        }
      )
  })
  it('should add currentPlay to radio when received a start music event', () => {
    expect(
      reducer(
        {
          radios: {
            "A": {
              "id": "A",
              "name": "Awersome radio",
              "urlToListen": "http://radio.fake",
              "playlist": []
            }
          },
          radioSelected: "A",
          musicsSubmitted: {}
        },
        startMusic({
          "radioId": "A",
          "id": "musicB",
          "title": "fake music",
          "source": "YOUTUBE",
          "location": "abcdefghijk"
        })
      )
    ).toEqual(
      {
        radios: {
          "A": {
            "id": "A",
            "name": "Awersome radio",
            "urlToListen": "http://radio.fake",
            "currentPlay": {
              "radioId": "A",
              "id": "musicB",
              "title": "fake music",
              "source": "YOUTUBE",
              "location": "abcdefghijk"
            },
            "playlist": []
          }
        },
        radioSelected: "A",
        musicsSubmitted: {}
      }
    )
  })
  it('should add music to playlist of radio when received a validate music event', () => {
    expect(
      reducer(
        {
          radios: {
            "A": {
              "id": "A",
              "name": "Awersome radio",
              "urlToListen": "http://radio.fake",
              "playlist": []
            }
          },
          radioSelected: "A",
          musicsSubmitted: {}
        },
        validateMusic({
          "radioId": "A",
          "id": "musicB",
          "title": "fake music",
          "source": "YOUTUBE",
          "location": "abcdefghijk"
        })
      )
    ).toEqual(
      {
        radios: {
          "A": {
            "id": "A",
            "name": "Awersome radio",
            "urlToListen": "http://radio.fake",
            "playlist": [{
              "radioId": "A",
              "id": "musicB",
              "title": "fake music",
              "source": "YOUTUBE",
              "location": "abcdefghijk"
            }]
          }
        },
        radioSelected: "A",
        musicsSubmitted: {}
      }
    )
  })
  it('should add music to currentPlay of radio when received a start music event and music is already in playlist', () => {
    expect(
      reducer(
        {
          radios: {
            "A": {
              "id": "A",
              "name": "Awersome radio",
              "urlToListen": "http://radio.fake",
              "playlist": [{
                "radioId": "A",
                "id": "musicB",
                "title": "fake music",
                "source": "YOUTUBE",
                "location": "abcdefghijk"
              }]
            }
          },
          radioSelected: "A",
          musicsSubmitted: {}
        },
        startMusic({
          "radioId": "A",
          "id": "musicB",
          "title": "fake music",
          "source": "YOUTUBE",
          "location": "abcdefghijk"
        })
      )
    ).toEqual(
      {
        radios: {
          "A": {
            "id": "A",
            "name": "Awersome radio",
            "urlToListen": "http://radio.fake",
            "currentPlay": {
              "radioId": "A",
              "id": "musicB",
              "title": "fake music",
              "source": "YOUTUBE",
              "location": "abcdefghijk"
            },
            "playlist": []
          }
        },
        radioSelected: "A",
        musicsSubmitted: {}
      }
    )
  })

  it('should add music to currentPlay of radio when received a start music event and music is already in playlist which contain other musics', () => {
    expect(
      reducer(
        {
          radios: {
            "A": {
              "id": "A",
              "name": "Awersome radio",
              "urlToListen": "http://radio.fake",
              "playlist": [
                {
                  "radioId": "A",
                  "id": "musicB",
                  "title": "fake music",
                  "source": "YOUTUBE",
                  "location": "abcdefghijk"
                },
                {
                  "radioId": "A",
                  "id": "musicC",
                  "title": "fake music C",
                  "source": "YOUTUBE",
                  "location": "abcdefghij1"
                },
                {
                  "radioId": "A",
                  "id": "musicD",
                  "title": "fake music D",
                  "source": "YOUTUBE",
                  "location": "abcdefghij2"
                }
              ]
            }
          },
          radioSelected: "A",
          musicsSubmitted: {}
        },
        startMusic({
          "radioId": "A",
          "id": "musicB",
          "title": "fake music",
          "source": "YOUTUBE",
          "location": "abcdefghijk"
        })
      )
    ).toEqual(
      {
        radios: {
          "A": {
            "id": "A",
            "name": "Awersome radio",
            "urlToListen": "http://radio.fake",
            "currentPlay": {
              "radioId": "A",
              "id": "musicB",
              "title": "fake music",
              "source": "YOUTUBE",
              "location": "abcdefghijk"
            },
            "playlist": [
              {
                "radioId": "A",
                "id": "musicC",
                "title": "fake music C",
                "source": "YOUTUBE",
                "location": "abcdefghij1"
              },
              {
                "radioId": "A",
                "id": "musicD",
                "title": "fake music D",
                "source": "YOUTUBE",
                "location": "abcdefghij2"
              }
            ]
          }
        },
        radioSelected: "A",
        musicsSubmitted: {}
      }
    )
  })
  it('should remove music from playlist in middle of playlist when receive a start', () => {
    expect(
      reducer(
        {
          radios: {
            "A": {
              "id": "A",
              "name": "Awersome radio",
              "urlToListen": "http://radio.fake",
              "playlist": [,
                {
                  "radioId": "A",
                  "id": "musicC",
                  "title": "fake music C",
                  "source": "YOUTUBE",
                  "location": "abcdefghij1"
                },
                {
                  "radioId": "A",
                  "id": "musicB",
                  "title": "fake music",
                  "source": "YOUTUBE",
                  "location": "abcdefghijk"
                },
                {
                  "radioId": "A",
                  "id": "musicD",
                  "title": "fake music D",
                  "source": "YOUTUBE",
                  "location": "abcdefghij2"
                }
              ]
            }
          },
          radioSelected: "A",
          musicsSubmitted: {}
        },
        startMusic({
          "radioId": "A",
          "id": "musicB",
          "title": "fake music",
          "source": "YOUTUBE",
          "location": "abcdefghijk"
        })
      )
    ).toEqual(
      {
        radios: {
          "A": {
            "id": "A",
            "name": "Awersome radio",
            "urlToListen": "http://radio.fake",
            "currentPlay": {
              "radioId": "A",
              "id": "musicB",
              "title": "fake music",
              "source": "YOUTUBE",
              "location": "abcdefghijk"
            },
            "playlist": [
              {
                "radioId": "A",
                "id": "musicC",
                "title": "fake music C",
                "source": "YOUTUBE",
                "location": "abcdefghij1"
              },
              {
                "radioId": "A",
                "id": "musicD",
                "title": "fake music D",
                "source": "YOUTUBE",
                "location": "abcdefghij2"
              }
            ]
          }
        },
        radioSelected: "A",
        musicsSubmitted: {}
      }
    )
  })
  it('should remove music from playlist at the end of playlist when receive a start', () => {
    expect(
      reducer(
        {
          radios: {
            "A": {
              "id": "A",
              "name": "Awersome radio",
              "urlToListen": "http://radio.fake",
              "playlist": [
                {
                  "radioId": "A",
                  "id": "musicC",
                  "title": "fake music C",
                  "source": "YOUTUBE",
                  "location": "abcdefghij1"
                },
                {
                  "radioId": "A",
                  "id": "musicD",
                  "title": "fake music D",
                  "source": "YOUTUBE",
                  "location": "abcdefghij2"
                },
                {
                  "radioId": "A",
                  "id": "musicB",
                  "title": "fake music",
                  "source": "YOUTUBE",
                  "location": "abcdefghijk"
                }
              ]
            }
          },
          radioSelected: "A",
          musicsSubmitted: {}
        },
        startMusic({
          "radioId": "A",
          "id": "musicB",
          "title": "fake music",
          "source": "YOUTUBE",
          "location": "abcdefghijk"
        })
      )
    ).toEqual(
      {
        radios: {
          "A": {
            "id": "A",
            "name": "Awersome radio",
            "urlToListen": "http://radio.fake",
            "currentPlay": {
              "radioId": "A",
              "id": "musicB",
              "title": "fake music",
              "source": "YOUTUBE",
              "location": "abcdefghijk"
            },
            "playlist": [
              {
                "radioId": "A",
                "id": "musicC",
                "title": "fake music C",
                "source": "YOUTUBE",
                "location": "abcdefghij1"
              },
              {
                "radioId": "A",
                "id": "musicD",
                "title": "fake music D",
                "source": "YOUTUBE",
                "location": "abcdefghij2"
              }
            ]
          }
        },
        radioSelected: "A",
        musicsSubmitted: {}
      }
    )
  })
})
