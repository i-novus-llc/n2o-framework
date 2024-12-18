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

import { Factory } from '../../core/factory/Factory'
import { PAGES } from '../../core/factory/factoryLevels'
import {
    makePageDisabledByIdSelector,
} from '../../ducks/pages/selectors'
import { Spinner } from '../snippets/Spinner/Spinner'
import { ErrorContainer } from '../../core/error/Container'

import withMetadata from './withMetadata'

function Page(props) {
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

Page.propTypes = {
    metadata: PropTypes.object,
    error: PropTypes.object,
    loading: PropTypes.bool,
}

export { Page }

const mapStateToProps = createStructuredSelector({
    disabled: (state, { pageId }) => makePageDisabledByIdSelector(pageId)(state),
})

export default compose(
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
)(Page)
