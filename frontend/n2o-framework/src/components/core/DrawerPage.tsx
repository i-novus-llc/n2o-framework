import React, { useCallback, ReactNode } from 'react'
import get from 'lodash/get'
import classNames from 'classnames'
import flowRight from 'lodash/flowRight'

import { Drawer, Props as DrawerProps } from '../snippets/Drawer/Drawer'
import { Spinner, SpinnerType } from '../snippets/Spinner/Spinner'
import Toolbar, { type ToolbarProps } from '../buttons/Toolbar'

import withOverlayMethods from './withOverlayMethods'
import Page from './Page'

interface Props {
    pageId: string
    visible?: boolean
    modalHeaderTitle: string
    toolbar?: {
        bottomLeft?: ToolbarProps
        bottomCenter?: ToolbarProps
        bottomRight?: ToolbarProps
    }
    disabled?: boolean
    closeOverlay(prompt?: boolean): void
    footer?: ReactNode
    prompt: boolean
    loading?: boolean
    src?: string
    pageUrl?: string
    pathMapping?: Record<string, string>
    queryMapping?: Record<string, string>
    entityKey?: string
    width?: string
    height?: string
    placement?: DrawerProps['placement']
    backdrop?: boolean
    level?: string | string[]
    closeOnBackdrop?: boolean
    animation?: boolean
    closeOnEscape?: boolean
    closable?: boolean
    fixedFooter: boolean
    renderFromSrc(src: string): ReactNode
}

function DrawerPage(props: Props) {
    const {
        src,
        pageUrl,
        pageId,
        pathMapping,
        queryMapping,
        visible,
        loading,
        modalHeaderTitle,
        footer,
        disabled,
        toolbar,
        entityKey,
        width,
        height,
        placement,
        backdrop,
        level,
        closeOnBackdrop,
        animation,
        closeOverlay,
        fixedFooter,
        closeOnEscape,
        closable,
        renderFromSrc,
        prompt,
    } = props

    const pageMapping = { pathMapping, queryMapping }

    const showSpinner = !visible || loading || typeof loading === 'undefined'
    const classes = classNames({ 'd-none': loading })
    const withToolbar = get(props, 'metadata.src') !== 'SearchablePage'
    const handleCloseOverlay = useCallback(() => closeOverlay(prompt), [closeOverlay, prompt])

    const renderPage = () => {
        if (pageUrl) {
            return (
                <Page
                    pageUrl={pageUrl}
                    pageId={pageId}
                    pageMapping={pageMapping}
                    entityKey={entityKey}
                    needMetadata
                    withToolbar={withToolbar}
                    initSearchValue=""
                    isDrawerPage
                />
            )
        }

        if (src) { return renderFromSrc(src) }

        return null
    }

    return (
        <div className="drawer-page-overlay">
            <Spinner
                className="drawer-spinner"
                loading={showSpinner}
                type={SpinnerType.cover}
                color="light"
                transparent
            >
                <Drawer
                    visible={!loading && visible !== false}
                    onHandleClick={handleCloseOverlay}
                    onClose={handleCloseOverlay}
                    title={modalHeaderTitle}
                    backdrop={backdrop}
                    width={width}
                    height={height}
                    placement={placement}
                    level={level}
                    closeOnBackdrop={closeOnBackdrop}
                    animation={animation}
                    fixedFooter={fixedFooter}
                    closeOnEscape={closeOnEscape}
                    closable={closable}
                    footer={
                        toolbar ? (
                            <div className={classNames('n2o-modal-actions', { 'n2o-disabled': disabled })}>
                                <Toolbar toolbar={toolbar.bottomLeft} entityKey={entityKey} />
                                <Toolbar toolbar={toolbar.bottomCenter} entityKey={entityKey} />
                                <Toolbar toolbar={toolbar.bottomRight} entityKey={entityKey} />
                            </div>
                        ) : footer
                    }
                >
                    <div className={classes}>{renderPage()}</div>
                </Drawer>
            </Spinner>
        </div>
    )
}

export const DrawerWindow = DrawerPage

export default flowRight(withOverlayMethods)(DrawerPage)
