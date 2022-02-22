import React from 'react'
import { compose, lifecycle, withHandlers } from 'recompose'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'
import get from 'lodash/get'

import {
    registerRegion,
    setActiveRegion,
    mapUrl,
    makeRegionIsInitSelector,
    makeRegionActiveEntitySelector,
} from '../../ducks/regions/store'

const createRegionContainer = config => (WrappedComponent) => {
    const { listKey } = config

    const RegionContainer = props => <WrappedComponent {...props} />

    const mapStateToProps = createStructuredSelector({
        isInit: (state, props) => makeRegionIsInitSelector(props.id)(state),
        activeEntity: (state, props) => makeRegionActiveEntitySelector(props.id)(state),
    })

    const mapDispatchToProps = dispatch => ({
        dispatch,
    })

    const enhance = compose(
        connect(
            mapStateToProps,
            mapDispatchToProps,
        ),
        withHandlers({
            initIfNeeded: props => () => {
                const {
                    dispatch,
                    id,
                    activeEntity,
                    lazy,
                    alwaysRefresh,
                } = props

                dispatch(registerRegion(id, {
                    regionId: id,
                    activeEntity,
                    isInit: true,
                    lazy,
                    alwaysRefresh,
                    [listKey]: get(props, listKey, []),
                }))
            },
            changeActiveEntity: props => (value) => {
                const { dispatch, id } = props

                dispatch(setActiveRegion(id, value))
                dispatch(mapUrl(value))
            },
        }),
        lifecycle({
            componentDidMount() {
                const { initIfNeeded } = this.props

                initIfNeeded()
            },
        }),
    )

    return enhance(RegionContainer)
}

export default createRegionContainer
