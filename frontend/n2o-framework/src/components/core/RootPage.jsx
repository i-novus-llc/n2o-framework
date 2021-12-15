import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { compose, defaultProps, getContext } from 'recompose'
import { createStructuredSelector } from 'reselect'

import { rootPageSelector } from '../../ducks/global/store'

// eslint-disable-next-line import/no-named-as-default
import Page from './Page'
// eslint-disable-next-line import/no-named-as-default
import OverlayPages from './OverlayPages'
import { GlobalAlertsConnected } from './GlobalAlerts'
import { SimpleTemplate } from './templates'

function RootPage({ rootPageId, defaultTemplate, match: { params } }) {
    const Template = defaultTemplate
    const rootPageUrl = params.pageUrl ? `/${params.pageUrl}` : '/'

    return (
        <Template>
            <GlobalAlertsConnected />
            <Page rootPage pageId={rootPageId} pageUrl={rootPageUrl} />
            <OverlayPages />
        </Template>
    )
}

RootPage.propTypes = {
    defaultTemplate: PropTypes.oneOfType([
        PropTypes.func,
        PropTypes.element,
        PropTypes.node,
    ]),
    rootPageId: PropTypes.string,
    match: PropTypes.object,
}

const mapStateToProps = createStructuredSelector({
    rootPageId: rootPageSelector,
})

export default compose(
    defaultProps({
        defaultTemplate: SimpleTemplate,
    }),
    getContext({
        defaultTemplate: PropTypes.oneOfType([
            PropTypes.func,
            PropTypes.element,
            PropTypes.node,
        ]),
    }),
    connect(mapStateToProps),
)(RootPage)
