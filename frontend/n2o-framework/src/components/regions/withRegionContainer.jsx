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

import { getFirstContentId } from './helpers'

const createRegionContainer = config => (WrappedComponent) => {
    const { listKey } = config

    const RegionContainer = props => <WrappedComponent {...props} />

    const mapStateToProps = createStructuredSelector({
        isInit: (state, props) => makeRegionIsInitSelector(props.id)(state),
        activeEntity: (state, props) => makeRegionActiveEntitySelector(props.id)(state),
        resolveModel: state => get(state, 'models.resolve', {}),
        query: state => get(state, 'router.location.query', {}),
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
                    active,
                    resolveModel,
                    activeTabFieldId,
                    query,
                    content,
                    datasource = null,
                    open = true,
                } = props

                const getCurrentActiveEntity = () => {
                    if (!open) {
                        return undefined
                    }

                    if (datasource && activeTabFieldId) {
                        const activeFromResolve = get(resolveModel, `${datasource}.${activeTabFieldId}`, null)

                        if (activeFromResolve) {
                            return activeFromResolve
                        }
                    }

                    return activeEntity || active || query[id] || getFirstContentId(content)
                }

                const currentActiveEntity = getCurrentActiveEntity()

                dispatch(registerRegion(id, {
                    regionId: id,
                    activeEntity: currentActiveEntity,
                    isInit: true,
                    lazy,
                    alwaysRefresh,
                    [listKey]: get(props, listKey, []),
                    datasource,
                    activeTabFieldId,
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
