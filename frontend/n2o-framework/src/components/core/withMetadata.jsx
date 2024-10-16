import React from 'react'
import { connect } from 'react-redux'
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
} from '../../ducks/pages/store'
import {
    makePageMetadataByIdSelector,
    makePageLoadingByIdSelector,
    makePageErrorByIdSelector,
} from '../../ducks/pages/selectors'
import { getLocation } from '../../ducks/global/selectors'

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
            const { metadata, error, reset, pageUrl } = this.props

            if (
                isEqual(metadata, prevProps.metadata) &&
                !isEmpty(metadata) &&
                !isEqual(pageUrl, prevProps.pageUrl) &&
                this.shouldGetPageMetadata(prevProps)
            ) {
                reset(prevProps.pageId)
                this.fetchMetadata()
            } else if (
                this.isEqualPageId(prevProps) &&
                !this.isEqualPageUrl(prevProps) &&
                error
            ) {
                this.fetchMetadata()
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

            if (!isEmpty(metadata?.routes)) {
                if (state?.silent) {
                    return false
                }
                const findedRoutes = filter(metadata.routes.list, (route) => {
                    const re = pathToRegexp(route.path)

                    return re.test(pathname)
                })
                const isNewPage = find(findedRoutes, route => route.isOtherPage)

                return (
                    isNewPage ||
                    (
                        this.isEqualPageId(prevProps) &&
                        !this.isEqualPageUrl(prevProps) &&
                        isEmpty(findedRoutes)
                    )
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

    ComponentWithMetadata.defaultProps = {
        rootPage: false,
    }

    const mapStateToProps = createStructuredSelector({
        metadata: (state, { pageId }) => makePageMetadataByIdSelector(pageId)(state),
        loading: (state, { pageId }) => makePageLoadingByIdSelector(pageId)(state),
        error: (state, { pageId }) => makePageErrorByIdSelector(pageId)(state),
        location: getLocation,
    })

    function mapDispatchToProps(dispatch) {
        return {
            getMetadata: (pageId, pageUrl, pageMapping, rootPage) => dispatch(
                metadataRequest(
                    pageId,
                    rootPage,
                    pageUrl,
                    pageMapping,
                ),
            ),
            reset: pageId => dispatch(resetPage(pageId)),
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
