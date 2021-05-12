import React, { PureComponent } from 'react'

import { id } from '../../../../../utils/id'

export const MODIFIERS = {
    preventOverflow: {
        boundariesElement: 'window',
    },
}

export const initUid = WrappedComponenet => class extends PureComponent {
    constructor(props) {
        super(props)
        this.uId = id()
    }

    render() {
        return <WrappedComponenet uId={this.uId} {...this.props} />
    }
}
