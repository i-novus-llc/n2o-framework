import React from 'react'
import { connect } from 'react-redux'
import { compose, pure } from 'recompose'
import { createStructuredSelector } from 'reselect'
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

import { resolvePath } from './router/resolvePath'
import { PageContext } from './router/context'

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
                parentId,
            } = this.props

            getMetadata(pageId, pageUrl, pageMapping, rootPage, parentId)
        }

        shouldGetPageMetadata(prevProps) {
            const {
                metadata,
                location: { pathname, state = {} },
            } = this.props

            if (state?.silent) { return false }

            const { subRoutes, path } = metadata?.routes || {}

            if (typeof path === 'string') {
                if (path.replace(/\/$/i, '') === pathname.replace(/\/$/i, '')) { return false }

                const isSubPage = subRoutes?.some(route => pathname.startsWith(resolvePath(path, route)))

                if (isSubPage) { return false }
            }

            return (
                this.isEqualPageId(prevProps) &&
                !this.isEqualPageUrl(prevProps)
            )
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
            const { pageId } = this.props
            const context = {
                pageId,
            }

            return (
                <PageContext.Provider value={context}>
                    <Component {...this.props} />
                </PageContext.Provider>
            )
        }
    }

    const mapStateToProps = createStructuredSelector({
        metadata: (state, { pageId }) => makePageMetadataByIdSelector(pageId)(state),
        loading: (state, { pageId }) => makePageLoadingByIdSelector(pageId)(state),
        error: (state, { pageId }) => makePageErrorByIdSelector(pageId)(state),
        location: getLocation,
    })

    function mapDispatchToProps(dispatch) {
        return {
            getMetadata: (pageId, pageUrl, pageMapping, rootPage, parentId) => dispatch(
                metadataRequest(
                    pageId,
                    rootPage,
                    pageUrl,
                    pageMapping,
                    parentId,
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
