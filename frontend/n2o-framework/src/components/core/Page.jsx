import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import get from 'lodash/get'
import { createStructuredSelector } from 'reselect'
import {
    compose,
    withPropsOnChange,
    branch,
    getContext,
    defaultProps,
    mapProps,
} from 'recompose'

// eslint-disable-next-line import/no-named-as-default
import Factory from '../../core/factory/Factory'
import { PAGES } from '../../core/factory/factoryLevels'
import {
    makePageDisabledByIdSelector,
} from '../../ducks/pages/selectors'
import { rootPageSelector } from '../../ducks/global/selectors'
import { Spinner } from '../snippets/Spinner/Spinner'
import { ErrorContainer } from '../../core/error/Container'

import withMetadata from './withMetadata'
import withActions from './withActions'
// eslint-disable-next-line import/no-cycle
import Root from './Root'

function Page(props, context) {
    const {
        metadata,
        loading,
        defaultTemplate: Template = React.Fragment,
        page,
        rootPage,
        error,
    } = props

    const renderDefaultBody = () => {
        const { defaultPage: contextDefaultPage } = context
        const defaultPage = get(metadata, 'src', contextDefaultPage)
        const regions = get(metadata, 'regions', {})

        return (
            <ErrorContainer error={error}>
                <Factory
                    id={get(metadata, 'id')}
                    src={defaultPage}
                    level={PAGES}
                    regions={regions}
                    {...props}
                />
            </ErrorContainer>
        )
    }

    return rootPage ? (
        <Root>
            <Template>
                <Spinner type="cover" loading={loading}>
                    {page ? React.createElement(page, props) : renderDefaultBody()}
                </Spinner>
            </Template>
        </Root>
    ) : (
        <Spinner type="cover" loading={loading}>
            {page ? React.createElement(page, props) : renderDefaultBody()}
        </Spinner>
    )
}

Page.contextTypes = {
    defaultPage: PropTypes.string,
}

Page.propTypes = {
    metadata: PropTypes.object,
    error: PropTypes.object,
    loading: PropTypes.bool,
    page: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
    defaultTemplate: PropTypes.any,
    rootPage: PropTypes.bool,
}

export { Page }

const mapStateToProps = createStructuredSelector({
    disabled: (state, { pageId }) => makePageDisabledByIdSelector(pageId)(state),
    rootPageId: rootPageSelector,
})

export default compose(
    connect(mapStateToProps),
    mapProps(props => ({
        ...props,
        pageUrl: props.pageUrl || `/${get(props, 'match.params.pageUrl', '')}`,
    })),
    withPropsOnChange(
        ['pageId', 'pageUrl', 'rootPageId'],
        ({ pageId, pageUrl, rootPageId, rootPage }) => ({
            pageId: (rootPage && rootPageId) || pageId || pageUrl || null,
            pageUrl: pageUrl || '/',
        }),
    ),
    branch(({ needMetadata }) => needMetadata, withMetadata),
    branch(({ rootPage }) => rootPage, withActions),
    getContext({
        defaultTemplate: PropTypes.oneOfType([
            PropTypes.func,
            PropTypes.element,
            PropTypes.node,
        ]),
        defaultPage: PropTypes.string,
    }),
    defaultProps({
        metadata: {},
        spinner: {},
        loading: false,
        disabled: false,
    }),
)(Page)
