import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import get from 'lodash/get'
import { createStructuredSelector } from 'reselect'
import {
    compose,
    withPropsOnChange,
    defaultProps,
} from 'recompose'
import { Spinner } from '@i-novus/n2o-components/lib/layouts/Spinner/Spinner'

import { Factory } from '../../core/factory/Factory'
import { PAGES } from '../../core/factory/factoryLevels'
import {
    makePageDisabledByIdSelector,
} from '../../ducks/pages/selectors'
import { ErrorContainer } from '../../core/error/Container'

import withMetadata from './withMetadata'
import withActions from './withActions'

function PageBody(props) {
    const {
        metadata,
        loading,
        error,
    } = props
    const src = get(metadata, 'src')
    const regions = get(metadata, 'regions', {})

    return (
        <Spinner type="cover" loading={loading}>
            <ErrorContainer error={error}>
                <Factory
                    id={get(metadata, 'id')}
                    src={src}
                    level={PAGES}
                    regions={regions}
                    {...props}
                />
            </ErrorContainer>
        </Spinner>
    )
}

PageBody.propTypes = {
    metadata: PropTypes.object,
    error: PropTypes.object,
    loading: PropTypes.bool,
}

export { PageBody }

const mapStateToProps = createStructuredSelector({
    disabled: (state, { pageId }) => makePageDisabledByIdSelector(pageId)(state),
})

export const Page = compose(
    connect(mapStateToProps),
    withPropsOnChange(
        ['pageId', 'pageUrl'],
        ({ pageId, pageUrl }) => ({
            pageId: pageId || pageUrl || null,
            pageUrl: pageUrl || '/',
        }),
    ),
    withMetadata,
    withActions,
    defaultProps({
        metadata: {},
        spinner: {},
        loading: false,
        disabled: false,
    }),
)(PageBody)

export const OverlayPage = compose(
    connect(mapStateToProps),
    withPropsOnChange(
        ['pageId', 'pageUrl'],
        ({ pageId, pageUrl }) => ({
            pageId: pageId || pageUrl || null,
            pageUrl: pageUrl || '/',
        }),
    ),
    withMetadata,
    defaultProps({
        metadata: {},
        spinner: {},
        loading: false,
        disabled: false,
    }),
)(PageBody)
