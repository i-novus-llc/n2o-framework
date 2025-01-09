import React, { memo, ComponentType } from 'react'
import { connect } from 'react-redux'
import flowRight from 'lodash/flowRight'

import { makePageToolbarByIdSelector } from '../../ducks/pages/selectors'
import { State } from '../../ducks/State'

export interface WithActionsProps { pageId: string, pageUrl: string }

const WithProps = (Component: ComponentType<WithActionsProps>) => {
    return (props: WithActionsProps) => {
        const { pageId, pageUrl } = props

        const enhancedProps = { ...props, pageId: !pageId && pageUrl ? pageUrl.substr(1) : pageId }

        return <Component {...enhancedProps} />
    }
}

export const WithActions = <P extends WithActionsProps>(Component: ComponentType<P>) => {
    const Wrapper = memo<P>(props => <Component {...props} />)

    const mapStateToProps = (state: State, { pageId }: WithActionsProps) => ({
        entityKey: pageId,
        toolbar: makePageToolbarByIdSelector(pageId)(state),
    })

    return flowRight(WithProps, memo, connect(mapStateToProps))(Wrapper)
}

export default WithActions
