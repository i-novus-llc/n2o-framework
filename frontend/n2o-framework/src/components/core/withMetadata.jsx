import React from 'react'
import { connect } from 'react-redux'
import { batchActions } from 'redux-batched-actions'
import PropTypes from 'prop-types'
import { compose, pure } from 'recompose'
import { createStructuredSelector } from 'reselect'
import pathToRegexp from 'path-to-regexp'
import filter from 'lodash/filter'
import find from 'lodash/find'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'

import {
    metadataRequest,
    resetPage,
    mapUrl,
} from '../../ducks/pages/store'
import {
    makePageMetadataByIdSelector,
    makePageLoadingByIdSelector,
    makePageErrorByIdSelector,
} from '../../ducks/pages/selectors'
import { getLocation } from '../../ducks/global/store'

const withMetadata = (Component) => {
    class ComponentWithMetadata extends React.Component {
        componentDidMount() {
            this.fetchMetadata()
        }

        componentWillUnmount() {
            const { pageId, reset } = this.props

            reset(pageId)
        }

        componentDidUpdate(prevProps) {
            const { pageId, metadata, error, routeMap, reset } = this.props

            if (
                isEqual(metadata, prevProps.metadata) &&
                !isEmpty(metadata) &&
                this.shouldGetPageMetadata(prevProps)
            ) {
                reset(prevProps.pageId)
                this.fetchMetadata()
            } else if (
                this.isEqualPageId(prevProps) &&
                !this.isEqualPageUrl(prevProps)
            ) {
                routeMap(pageId)
                if (error) {
                    this.fetchMetadata()
                }
            }
        }

        fetchMetadata() {
            const {
                pageId,
                pageUrl,
                pageMapping,
                rootPage,
                getMetadata,
            } = this.props

            getMetadata(pageId, pageUrl, pageMapping, rootPage)
        }

        shouldGetPageMetadata(prevProps) {
            const {
                metadata,
                location: { pathname, state = {} },
            } = this.props

            if (!isEmpty(metadata) && !isEmpty(metadata.routes)) {
                const findedRoutes = filter(metadata.routes.list, (route) => {
                    const re = pathToRegexp(route.path)

                    return re.test(pathname)
                })
                const isNewPage = find(findedRoutes, route => route.isOtherPage)

                return (
                    (
                        isNewPage || (
                            this.isEqualPageId(prevProps) &&
                            !this.isEqualPageUrl(prevProps) &&
                            isEmpty(findedRoutes)
                        )
                    ) && !state.silent
                )
            }

            return false
        }

        isEqualPageId(prevProps) {
            const { pageId } = this.props

            return pageId === prevProps.pageId
        }

        isEqualPageUrl(prevProps) {
            const { pageUrl } = this.props

            return pageUrl === prevProps.pageUrl
        }

        render() {
            return <Component {...this.props} />
        }
    }

    ComponentWithMetadata.propTypes = {
        pageId: PropTypes.string,
        pageUrl: PropTypes.string,
        pageMapping: PropTypes.object,
        rootPage: PropTypes.bool,
        metadata: PropTypes.object,
        loading: PropTypes.bool,
        error: PropTypes.oneOfType([PropTypes.object, PropTypes.oneOf([false])]),
        location: PropTypes.object,
        getMetadata: PropTypes.func,
        reset: PropTypes.func,
        routeMap: PropTypes.func,
    }

    ComponentWithMetadata.defaultProps = {
        rootPage: false,
    }

    const mapStateToProps = createStructuredSelector({
        metadata: (state, props) => makePageMetadataByIdSelector(props.pageId)(state, props),
        loading: (state, props) => makePageLoadingByIdSelector(props.pageId)(state, props),
        error: (state, { pageId }) => makePageErrorByIdSelector(pageId)(state),
        location: getLocation,
    })

    function mapDispatchToProps(dispatch) {
        return {
            getMetadata: (pageId, pageUrl, pageMapping, rootPage) => dispatch(metadataRequest(
                pageId, rootPage, pageUrl, pageMapping,
            )),
            reset: pageId => dispatch(batchActions([resetPage(pageId)])),
            routeMap: pageId => dispatch(mapUrl(pageId)),
        }
    }

    return compose(
        pure,
        connect(
            mapStateToProps,
            mapDispatchToProps,
        ),
    )(ComponentWithMetadata)
}

export default withMetadata
