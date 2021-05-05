import React from 'react'
import { compose, branch } from 'recompose'

import SimpleButton from '../Simple/Simple'

import withLinkAction from './withLinkAction'
import withPerformAction from './withPerformAction'

function StandardButton(props) {
    return <SimpleButton {...props} />
}

const enhance = compose(
    branch(({ url }) => !!url, withLinkAction, withPerformAction),
)

export default enhance(StandardButton)
