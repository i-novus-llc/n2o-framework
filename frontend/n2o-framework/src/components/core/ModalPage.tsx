import React, { CSSProperties } from 'react'
import { Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap'
import classNames from 'classnames'

import { Toolbar, ToolbarProps } from '../buttons/Toolbar'
import { ModelPrefix } from '../../core/datasource/const'
import { EMPTY_OBJECT } from '../../utils/emptyTypes'

import { OverlayPage } from './Page'
import { PageTitle } from './PageTitle'
import { WithOverlayMethods, type WithOverlayMethodsProps } from './withOverlayMethods'

interface Props extends WithOverlayMethodsProps {
    entityKey?: string
    toolbar?: {
        bottomLeft?: ToolbarProps;
        bottomCenter?: ToolbarProps;
        bottomRight?: ToolbarProps;
    };
    visible: boolean
    loading?: boolean
    src?: string
    pathMapping?: Record<string, string>
    queryMapping?: Record<string, string>
    size?: 'sm' | 'lg'
    disabled?: boolean
    scrollable?: boolean
    prompt?: boolean
    className?: string
    backdrop?: 'static' | true | false
    style?: CSSProperties
    hasHeader?: boolean
    metadata?: {
        page?: {
            modalHeaderTitle?: string
            datasource?: string
            model?: ModelPrefix
        };
    };
}

const ModalPage = ({
    entityKey,
    toolbar,
    visible,
    loading,
    pageUrl,
    pageId,
    src,
    pathMapping,
    queryMapping,
    scrollable,
    prompt,
    className,
    style,
    renderFromSrc,
    closeOverlay,
    size = 'lg',
    disabled = false,
    backdrop = 'static',
    hasHeader = false,
    metadata = EMPTY_OBJECT,
    ...rest
}: Props) => {
    const { page = {} } = metadata
    const { modalHeaderTitle, datasource, model: modelPrefix = ModelPrefix.active } = page

    const pageMapping = { pathMapping, queryMapping }

    const classes = classNames({ 'd-none': loading })

    const renderModalBody = () => {
        if (pageUrl) {
            return (
                <OverlayPage
                    pageUrl={pageUrl}
                    pageId={pageId}
                    pageMapping={pageMapping}
                    entityKey={entityKey}
                    routable={false}
                />
            )
        }

        if (src && renderFromSrc) { return renderFromSrc(src) }

        return null
    }

    return (
        <Modal
            {...rest}
            isOpen={visible}
            toggle={() => closeOverlay(prompt)}
            size={size}
            style={style}
            scrollable={scrollable}
            className={className}
            backdrop={backdrop}
        >
            {hasHeader && (
                <ModalHeader
                    className={classes}
                    toggle={() => closeOverlay(prompt)}
                >
                    <PageTitle
                        title={modalHeaderTitle}
                        datasource={datasource}
                        modelPrefix={modelPrefix}
                        titleLayout={false}
                    />
                </ModalHeader>
            )}

            <ModalBody className={classes}>{renderModalBody()}</ModalBody>

            {toolbar && (
                <ModalFooter className={classes}>
                    <div className={classNames('n2o-modal-actions', { 'n2o-disabled': disabled })}>
                        <Toolbar toolbar={toolbar.bottomLeft} entityKey={entityKey} />
                        <Toolbar toolbar={toolbar.bottomCenter} entityKey={entityKey} />
                        <Toolbar toolbar={toolbar.bottomRight} entityKey={entityKey} />
                    </div>
                </ModalFooter>
            )}
        </Modal>
    )
}

export default WithOverlayMethods(ModalPage)
