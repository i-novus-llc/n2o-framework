import React, { useCallback, ReactNode } from 'react'
import classNames from 'classnames'
import { Drawer, type Props as DrawerProps } from '@i-novus/n2o-components/lib/display/Drawer'
import { Spinner, SpinnerType } from '@i-novus/n2o-components/lib/layouts/Spinner/Spinner'

import Toolbar, { type ToolbarProps } from '../buttons/Toolbar'

import { WithOverlayMethods, type WithOverlayMethodsProps } from './withOverlayMethods'
import Page from './Page'

interface Props extends WithOverlayMethodsProps {
    visible?: boolean
    modalHeaderTitle: string
    toolbar?: {
        bottomLeft?: ToolbarProps
        bottomCenter?: ToolbarProps
        bottomRight?: ToolbarProps
    }
    disabled?: boolean
    footer?: ReactNode
    prompt: boolean
    loading?: boolean
    src?: string
    parentPage?: string
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
        parentPage,
    } = props

    const pageMapping = { pathMapping, queryMapping }

    const showSpinner = !visible || loading || typeof loading === 'undefined'
    const classes = classNames({ 'd-none': loading })
    const handleCloseOverlay = useCallback(() => closeOverlay(prompt), [closeOverlay, prompt])

    const renderPage = () => {
        if (pageUrl) {
            return (
                <Page
                    pageUrl={pageUrl}
                    pageId={pageId}
                    pageMapping={pageMapping}
                    entityKey={entityKey}
                    initSearchValue=""
                    parentPage={parentPage}
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

export default WithOverlayMethods(DrawerPage)
