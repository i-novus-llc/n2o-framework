import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import find from 'lodash/find'
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
    makePageStatusByIdSelected,
} from '../../ducks/pages/selectors'
import { rootPageSelector } from '../../ducks/global/store'
import { Spinner } from '../snippets/Spinner/Spinner'

import withMetadata from './withMetadata'
import withActions from './withActions'
import { SimpleTemplate } from './templates'
// eslint-disable-next-line import/no-cycle
import Root from './Root'

function Page(props, context) {
    const {
        metadata,
        loading,
        status,
        defaultTemplate: Template = React.Fragment,
        defaultErrorPages,
        page,
        rootPage,
    } = props

    const getErrorPage = () => get(
        find(defaultErrorPages, page => page.status === status),
        'component',
        null,
    )

    const errorPage = getErrorPage()

    const renderDefaultBody = () => {
        const { defaultPage: contextDefaultPage } = context
        const defaultPage = get(metadata, 'src', contextDefaultPage)
        const regions = get(metadata, 'regions', {})

        return errorPage ? (
            React.createElement(errorPage)
        ) : (
            <Factory
                id={get(metadata, 'id')}
                src={defaultPage}
                level={PAGES}
                regions={regions}
                {...props}
            />
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
    loading: PropTypes.bool,
    status: PropTypes.number,
    page: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
    defaultTemplate: PropTypes.any,
    defaultErrorPages: PropTypes.any,
    rootPage: PropTypes.bool,
}

export { Page }

const mapStateToProps = createStructuredSelector({
    disabled: (state, { pageId }) => makePageDisabledByIdSelector(pageId)(state),
    status: (state, { pageId }) => makePageStatusByIdSelected(pageId)(state),
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
        defaultBreadcrumb: PropTypes.oneOfType([
            PropTypes.func,
            PropTypes.element,
            PropTypes.node,
        ]),
        defaultErrorPages: PropTypes.arrayOf(
            PropTypes.oneOfType([PropTypes.node, PropTypes.element, PropTypes.func]),
        ),
        defaultTemplate: PropTypes.oneOfType([
            PropTypes.func,
            PropTypes.element,
            PropTypes.node,
        ]),
        defaultPage: PropTypes.string,
    }),
    defaultProps({
        defaultTemplate: SimpleTemplate,
        metadata: {},
        loading: false,
        disabled: false,
    }),
)(Page)
