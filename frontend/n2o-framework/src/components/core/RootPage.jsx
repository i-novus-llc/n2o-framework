import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import get from 'lodash/get'
import { createStructuredSelector } from 'reselect'
import {
    compose,
    withPropsOnChange,
    mapProps,
} from 'recompose'
import { Spinner } from '@i-novus/n2o-components/lib/layouts/Spinner/Spinner'

import { Factory } from '../../core/factory/Factory'
import { PAGES } from '../../core/factory/factoryLevels'
import {
    makePageDisabledByIdSelector,
} from '../../ducks/pages/selectors'
import { rootPageSelector } from '../../ducks/global/selectors'
import { ErrorContainer } from '../../core/error/Container'

import withMetadata from './withMetadata'
import OverlayPages from './OverlayPages'

function RootPage(props) {
    const {
        metadata,
        loading,
        defaultTemplate: Template = React.Fragment,
        error,
    } = props
    const src = get(metadata, 'src')
    const regions = get(metadata, 'regions', {})
    const toolbar = get(metadata, 'toolbar', {})

    return (
        <>
            <Template>
                <Spinner type="cover" loading={loading}>
                    <ErrorContainer error={error}>
                        <Factory
                            id={get(metadata, 'id')}
                            src={src}
                            level={PAGES}
                            regions={regions}
                            toolbar={toolbar}
                            rootPage
                            {...props}
                        />
                    </ErrorContainer>
                </Spinner>
            </Template>
            <OverlayPages />
        </>
    )
}

RootPage.propTypes = {
    metadata: PropTypes.object,
    error: PropTypes.object,
    loading: PropTypes.bool,
    defaultTemplate: PropTypes.any,
}

const mapStateToProps = createStructuredSelector({
    disabled: (state, { pageId }) => makePageDisabledByIdSelector(pageId)(state),
    rootPageId: rootPageSelector,
})

export default compose(
    connect(mapStateToProps),
    mapProps(props => ({
        ...props,
        rootPage: true,
        pageUrl: props.pageUrl || `/${get(props, 'match.params.pageUrl', '')}`,
    })),
    withPropsOnChange(
        ['pageId', 'pageUrl', 'rootPageId'],
        ({ pageId, pageUrl, rootPageId }) => ({
            pageId: rootPageId || pageId || pageUrl || null,
            pageUrl: pageUrl || '/',
        }),
    ),
    withMetadata,
)(RootPage)
