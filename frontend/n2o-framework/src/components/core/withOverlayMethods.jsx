import React, { Component } from 'react'
import { createStructuredSelector } from 'reselect'
import { compose } from 'recompose'
import { connect } from 'react-redux'
import get from 'lodash/get'
import { withTranslation } from 'react-i18next'

import {
    makePageDisabledByIdSelector,
    makePageLoadingByIdSelector,
    makePageMetadataByIdSelector,
    makePageTitleByIdSelector,
} from '../../ducks/pages/selectors'
import { makeShowPromptByName } from '../../ducks/overlays/selectors'

import withActions from './withActions'

function withOverlayMethods(WrappedComponent) {
    class OverlayMethods extends Component {
        closeOverlay = (prompt) => {
            const { name, close } = this.props

            close(name, prompt)
        }

        closePrompt = () => {
            const { name, hidePrompt } = this.props

            hidePrompt(name)
        }

        showPrompt = () => {
            const { t } = this.props

            if (window.confirm(t('defaultPromptMessage'))) {
                this.closeOverlay(false)

                return
            }

            this.closePrompt()
        }

        componentDidUpdate(prevProps) {
            const { showPrompt } = this.props

            if (showPrompt !== prevProps.showPrompt && showPrompt) {
                this.showPrompt()
            }
        }

        renderFromSrc(src) {
            const { resolveProps } = this.context
            const Component = resolveProps(src, null)

            return <Component />
        }

        render() {
            return (
                <WrappedComponent
                    {...this.props}
                    title={get(this.props, 'metadata.page.title', '')}
                    modalHeaderTitle={get(this.props, 'metadata.page.modalHeaderTitle', '')}
                    closeOverlay={this.closeOverlay}
                    renderFromSrc={this.renderFromSrc}
                />
            )
        }
    }

    const mapStateToProps = createStructuredSelector({
        title: (state, { pageId }) => makePageTitleByIdSelector(pageId)(state),
        loading: (state, { pageId }) => makePageLoadingByIdSelector(pageId)(state),
        disabled: (state, { pageId }) => makePageDisabledByIdSelector(pageId)(state),
        showPrompt: (state, { name }) => makeShowPromptByName(name)(state),
        metadata: (state, { pageId }) => makePageMetadataByIdSelector(pageId)(state),
    })

    return compose(
        withTranslation(),
        connect(mapStateToProps),
        withActions,
    )(OverlayMethods)
}

export default withOverlayMethods
