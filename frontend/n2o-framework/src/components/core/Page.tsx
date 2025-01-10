import React, { useMemo } from 'react'
import { connect } from 'react-redux'
import get from 'lodash/get'
import flowRight from 'lodash/flowRight'
import { SpinnerType } from '@i-novus/n2o-components/lib/layouts/Spinner/Spinner'

import { Factory } from '../../core/factory/Factory'
import { PAGES } from '../../core/factory/factoryLevels'
import { makePageDisabledByIdSelector } from '../../ducks/pages/selectors'
import { Spinner } from '../snippets/Spinner/Spinner'
import { ErrorContainer } from '../../core/error/Container'
import { State } from '../../ducks/State'

import { WithMetadata, type WithMetadataProps } from './withMetadata'

export interface PageProps extends WithMetadataProps {
    spinner: Record<string, unknown>
    disabled: boolean
}

function PageBody(props: PageProps) {
    const {
        error,
        pageId,
        pageUrl,
        metadata = {},
        loading = false,
        spinner = {},
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

export const Page = flowRight(connect(mapStateToProps), WithMetadata<PageProps>)(PageBody)
export default Page
