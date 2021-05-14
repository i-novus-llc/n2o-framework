import React from 'react'
import { createStructuredSelector } from 'reselect'
import { compose } from 'recompose'
import { connect } from 'react-redux'
import get from 'lodash/get'
import { withTranslation } from 'react-i18next'
import PropTypes from 'prop-types'

import {
    makePageDisabledByIdSelector,
    makePageLoadingByIdSelector,
    makePageMetadataByIdSelector,
    makePageTitleByIdSelector,
} from '../../selectors/pages'
import { makeShowPromptByName } from '../../selectors/overlays'

import withActions from './withActions'

function withOverlayMethods(WrappedComponent) {
    class OverlayMethods extends React.Component {
        constructor(props) {
            super(props)
            this.closeOverlay = this.closeOverlay.bind(this)
            this.closePrompt = this.closePrompt.bind(this)
            this.showPrompt = this.showPrompt.bind(this)
        }

        componentDidUpdate(prevProps) {
            const { showPrompt } = this.props

            if (
                showPrompt !== prevProps.showPrompt && showPrompt
            ) {
                this.showPrompt()
            }
        }

        renderFromSrc(src) {
            const { resolveProps } = this.context
            const Component = resolveProps(src, null)

            return <Component />
        }

        closeOverlay(prompt) {
            const { name, close } = this.props

            close(name, prompt)
        }

        closePrompt() {
            const { name, hidePrompt } = this.props

            hidePrompt(name)
        }

        showPrompt() {
            const { t } = this.props

            if (window.confirm(t('defaultPromptMessage'))) { // eslint-disable-line no-alert
                this.closeOverlay(false)
            } else {
                this.closePrompt()
            }
        }

        render() {
            return (
                <WrappedComponent
                    {...this.props}
                    title={get(this.props, 'metadata.page.title', '')}
                    closeOverlay={this.closeOverlay}
                    renderFromSrc={this.renderFromSrc}
                />
            )
        }
    }

    OverlayMethods.propTypes = {
        showPrompt: PropTypes.func,
        name: PropTypes.string,
        close: PropTypes.func,
        hidePrompt: PropTypes.func,
        t: PropTypes.func,
    }

    const mapStateToProps = createStructuredSelector({
        title: (state, { pageId }) => makePageTitleByIdSelector(pageId)(state),
        loading: (state, { pageId }) => makePageLoadingByIdSelector(pageId)(state),
        disabled: (state, { pageId }) => makePageDisabledByIdSelector(pageId)(state),
        showPrompt: (state, { name }) => makeShowPromptByName(name)(state),
        metadata: (state, props) => makePageMetadataByIdSelector(props.pageId)(state),
    })

    return compose(
        withTranslation(),
        connect(mapStateToProps),
        withActions,
    )(OverlayMethods)
}

export default withOverlayMethods
