import React, { memo, ComponentType } from 'react'
import { connect } from 'react-redux'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import flowRight from 'lodash/flowRight'
import { Dispatch } from 'redux'

import { metadataRequest, resetPage } from '../../ducks/pages/store'
import { makePageMetadataByIdSelector, makePageLoadingByIdSelector, makePageErrorByIdSelector } from '../../ducks/pages/selectors'
import { getLocation } from '../../ducks/global/selectors'
import { State } from '../../ducks/State'
import { Mapping } from '../../ducks/datasource/Provider'
import { Metadata } from '../../ducks/pages/Pages'
import type { ErrorContainerProps } from '../../core/error/types'

import { resolvePath } from './router/resolvePath'
import { PageContext } from './router/context'

export interface WithMetadataProps {
    pageId: string
    pageUrl: string
    pageMapping: Mapping
    rootPage: string
    parentId: string
    metadata: Metadata
    loading: boolean
    error: ErrorContainerProps['error']
    getMetadata(
        pageId: string,
        pageUrl: string,
        pageMapping: Mapping,
        rootPage: string,
        parentId: string
    ): void;
    reset(pageId: string): void
    location: Location & { state: { silent: boolean } }
}

export const WithMetadata = <P extends WithMetadataProps>(Component: ComponentType<P & WithMetadataProps>) => {
    class ComponentWithMetadata extends React.Component<P & WithMetadataProps> {
        componentDidMount() { this.fetchMetadata() }

        componentWillUnmount() {
            const { pageId, reset } = this.props

            reset(pageId)
        }

        componentDidUpdate(prevProps: WithMetadataProps) {
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

        shouldGetPageMetadata(prevProps: WithMetadataProps) {
            const { metadata, location: { pathname, state } } = this.props

            if (state?.silent) { return false }

            const { subRoutes, path } = metadata?.routes || {}

            if (typeof path === 'string') {
                if (path.replace(/\/$/i, '') === pathname.replace(/\/$/i, '')) { return false }

                const isSubPage = subRoutes?.some(route => pathname.startsWith(resolvePath(path, route)))

                if (isSubPage) { return false }
            }

            return this.isEqualPageId(prevProps) && !this.isEqualPageUrl(prevProps)
        }

        isEqualPageId(prevProps: WithMetadataProps) {
            const { pageId } = this.props

            return pageId === prevProps.pageId
        }

        isEqualPageUrl(prevProps: WithMetadataProps) {
            const { pageUrl } = this.props

            return pageUrl === prevProps.pageUrl
        }

        render() {
            const { pageId } = this.props
            const context = { pageId }

            return (
                <PageContext.Provider value={context}>
                    <Component {...this.props} />
                </PageContext.Provider>
            )
        }
    }

    const mapStateToProps = (state: State, { pageId }: WithMetadataProps) => ({
        metadata: makePageMetadataByIdSelector(pageId)(state),
        loading: makePageLoadingByIdSelector(pageId)(state),
        error: makePageErrorByIdSelector(pageId)(state),
        location: getLocation(state),
    })

    function mapDispatchToProps(dispatch: Dispatch) {
        return {
            getMetadata: (
                pageId: WithMetadataProps['pageId'],
                pageUrl: WithMetadataProps['pageUrl'],
                pageMapping: WithMetadataProps['pageMapping'],
                rootPage: WithMetadataProps['rootPage'],
                parentId: WithMetadataProps['parentId'],
            ) => dispatch(metadataRequest(pageId, rootPage, pageUrl, pageMapping, parentId)),
            reset: (pageId: WithMetadataProps['pageId']) => dispatch(resetPage(pageId)),
        }
    }

    return flowRight(memo, connect(mapStateToProps, mapDispatchToProps))(ComponentWithMetadata)
}

export default WithMetadata
