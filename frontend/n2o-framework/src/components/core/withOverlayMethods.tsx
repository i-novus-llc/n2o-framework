import React, { Component, ComponentType, ReactNode } from 'react'
import { connect } from 'react-redux'
import get from 'lodash/get'
import flowRight from 'lodash/flowRight'
import { withTranslation } from 'react-i18next'

import {
    makePageDisabledByIdSelector,
    makePageLoadingByIdSelector,
    makePageMetadataByIdSelector,
    makePageTitleByIdSelector,
} from '../../ducks/pages/selectors'
import { makeShowPromptByName } from '../../ducks/overlays/selectors'
import { type State } from '../../ducks/State'

import { WithActions, type WithActionsProps } from './withActions'

export interface WithOverlayMethodsProps extends WithActionsProps {
    title: string
    modalHeaderTitle: string
    closeOverlay(prompt?: boolean): void
    renderFromSrc?(src: string): ReactNode
    name: string
    close(name: string, prompt?: boolean): void
    hidePrompt(name: string): void
    showPrompt: boolean
    t(text: string): string
}

export function WithOverlayMethods<P>(WrappedComponent: ComponentType<WithOverlayMethodsProps & P>) {
    class OverlayMethods extends Component<P & WithOverlayMethodsProps> {
        closeOverlay = (prompt: boolean) => {
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

        componentDidUpdate(prevProps: WithOverlayMethodsProps) {
            const { showPrompt } = this.props

            if (showPrompt !== prevProps.showPrompt && showPrompt) { this.showPrompt() }
        }

        renderFromSrc(src: string) {
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

    const mapStateToProps = (state: State, { pageId, name }: WithOverlayMethodsProps) => ({
        title: makePageTitleByIdSelector(pageId)(state),
        loading: makePageLoadingByIdSelector(pageId)(state),
        disabled: makePageDisabledByIdSelector(pageId)(state),
        showPrompt: makeShowPromptByName(name)(state),
        metadata: makePageMetadataByIdSelector(pageId)(state),
    })

    return flowRight(
        withTranslation(),
        connect(mapStateToProps),
        WithActions<WithOverlayMethodsProps>,
    )(OverlayMethods)
}

export default WithOverlayMethods
