import React, { useMemo } from 'react'
import { connect } from 'react-redux'
import get from 'lodash/get'
import flowRight from 'lodash/flowRight'

import { Spinner, SpinnerType } from '../../factoryComponents/Spinner'
import { Factory } from '../../core/factory/Factory'
import { PAGES } from '../../core/factory/factoryLevels'
import { makePageDisabledByIdSelector } from '../../ducks/pages/selectors'
import { ErrorContainer } from '../../core/error/Container'
import { State } from '../../ducks/State'
import { EMPTY_OBJECT } from '../../utils/emptyTypes'

import { WithMetadata, type WithMetadataProps } from './withMetadata'
import { WithActions, type WithActionsProps } from './withActions'

type EnhancedProps = WithMetadataProps & WithActionsProps

export interface PageProps extends EnhancedProps {
    spinner: Record<string, unknown>
    disabled: boolean
}

function PageBody(props: PageProps) {
    const {
        error,
        pageId,
        pageUrl,
        metadata = EMPTY_OBJECT,
        loading = false,
        spinner = EMPTY_OBJECT,
        disabled = false,
    } = props

    const computedProps = useMemo(() => {
        return {
            pageId: pageId || pageUrl || null,
            pageUrl: pageUrl || '/',
        }
    }, [pageId, pageUrl])

    const { pageId: computedPageId, pageUrl: computedPageUrl } = computedProps

    return (
        <Spinner type={SpinnerType.cover} loading={loading}>
            <ErrorContainer error={error}>
                <Factory
                    level={PAGES}
                    id={get(metadata, 'id')}
                    src={get(metadata, 'src')}
                    regions={get(metadata, 'regions', {})}
                    {...props}
                    pageId={computedPageId}
                    pageUrl={computedPageUrl}
                    spinner={spinner}
                    disabled={disabled}
                />
            </ErrorContainer>
        </Spinner>
    )
}

const mapStateToProps = (state: State, { pageId }: PageProps) => ({ disabled: makePageDisabledByIdSelector(pageId)(state) })

export const Page = flowRight(
    connect(mapStateToProps),
    WithMetadata<PageProps>,
    WithActions<PageProps>,
)(PageBody)

export const OverlayPage = flowRight(
    connect(mapStateToProps),
    WithMetadata<PageProps>,
)(PageBody)
