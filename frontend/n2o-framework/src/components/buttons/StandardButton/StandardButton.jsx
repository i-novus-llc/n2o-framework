import React from 'react'
import { branch } from 'recompose'

import SimpleButton from '../Simple/Simple'

import { withLinkAction } from './withLinkAction'
import { withPerformAction } from './withPerformAction'

function StandardButton(props) {
    return <SimpleButton {...props} />
}

export default branch(({ url }) => !!url, withLinkAction, withPerformAction)(StandardButton)
