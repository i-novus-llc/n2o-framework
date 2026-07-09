import React, { Fragment, ExoticComponent, ReactNode, ComponentType, memo } from 'react'
import { connect } from 'react-redux'
import get from 'lodash/get'
import flowRight from 'lodash/flowRight'
import isEmpty from 'lodash/isEmpty'

import { Spinner, SpinnerType } from '../../factoryComponents/Spinner'
import { Factory } from '../../core/factory/Factory'
import { PAGES } from '../../core/factory/factoryLevels'
import { makePageDisabledByIdSelector } from '../../ducks/pages/selectors'
import { rootPageSelector } from '../../ducks/global/selectors'
import { ErrorContainer } from '../../core/error/Container'
import { State } from '../../ducks/State'

import { WithMetadata, type WithMetadataProps } from './withMetadata'
import OverlayPages from './OverlayPages'
import { type PageProps } from './Page'
import { useLocation } from './router/useLocation'

export interface RootPageProps extends WithMetadataProps {
    spinner: Record<string, unknown>
    disabled: boolean
    defaultTemplate: ExoticComponent<{ children?: ReactNode }>
    rootPageId: string
}

function RootPageBody(props: RootPageProps) {
    const {
        metadata,
        loading,
        defaultTemplate: Template = Fragment,
        error,
        pageUrl,
        rootPageId,
        pageId,
    } = props
    const src = get(metadata, 'src')
    const regions = get(metadata, 'regions', {})
    const toolbar = get(metadata, 'toolbar', {})

    return (
        <>
            <Template>
                <Spinner type={SpinnerType.cover} loading={loading}>
                    <ErrorContainer error={error}>
                        {!isEmpty(metadata) && (
                            <Factory
                                id={get(metadata, 'id')}
                                src={src}
                                level={PAGES}
                                regions={regions}
                                toolbar={toolbar}
                                {...props}
                                rootPageId={rootPageId}
                                pageId={pageId}
                                pageUrl={pageUrl}
                                rootPage
                            />
                        )}
                    </ErrorContainer>
                </Spinner>
            </Template>
            <OverlayPages />
        </>
    )
}

const mapStateToProps = (state: State, { pageId }: PageProps) => ({
    disabled: makePageDisabledByIdSelector(pageId)(state),
    rootPageId: rootPageSelector(state),
    rootPage: true,
})

function WithComputedProps(Component: ComponentType<RootPageProps>) {
    return memo((props: RootPageProps) => {
        const { pageUrl, rootPageId, pageId } = props
        const { pathname } = useLocation()
        const computedPageUrl = pageUrl || pathname || '/'
        const computedPageId = rootPageId || pageId || computedPageUrl || ''

        return <Component {...props} pageId={computedPageId} pageUrl={computedPageUrl} />
    })
}

export const RootPage = flowRight(
    connect(mapStateToProps),
    WithComputedProps,
    WithMetadata<RootPageProps>,
)(RootPageBody)

export default RootPage
