import React, { ReactNode, CSSProperties } from 'react'
import PropTypes from 'prop-types'
import { Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap'
import classNames from 'classnames'
import { compose } from 'recompose'

import { Toolbar, ToolbarProps } from '../buttons/Toolbar'
import { ModelPrefix } from '../../core/datasource/const'

import Page from './Page'
import { PageTitle } from './PageTitle'
import withOverlayMethods from './withOverlayMethods'

interface Props {
    entityKey?: string
    toolbar?: {
        bottomLeft?: ToolbarProps;
        bottomCenter?: ToolbarProps;
        bottomRight?: ToolbarProps;
    };
    visible: boolean
    loading?: boolean
    pageUrl?: string
    pageId: string
    src?: string
    pathMapping?: Record<string, string>
    queryMapping?: Record<string, string>
    size?: 'sm' | 'lg'
    disabled?: boolean
    scrollable?: boolean
    prompt?: string
    className?: string
    backdrop?: 'static' | true | false
    style?: CSSProperties
    hasHeader?: boolean
    renderFromSrc?(src: string): ReactNode
    closeOverlay(prompt?: string): void
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
    size,
    disabled,
    scrollable,
    prompt,
    className,
    backdrop,
    style,
    hasHeader,
    renderFromSrc,
    closeOverlay,
    metadata = {},
    ...rest
}: Props) => {
    const { page = {} } = metadata
    const { modalHeaderTitle, datasource, model: modelPrefix = ModelPrefix.active } = page

    const pageMapping = { pathMapping, queryMapping }

    const classes = classNames({ 'd-none': loading })

    const renderModalBody = () => {
        if (pageUrl) {
            return (
                <Page
                    pageUrl={pageUrl}
                    pageId={pageId}
                    pageMapping={pageMapping}
                    entityKey={entityKey}
                    routable={false}
                    needMetadata
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

            <ModalBody className={classes}>
                {renderModalBody()}
            </ModalBody>

            {toolbar && (
                <ModalFooter className={classes}>
                    <div
                        className={classNames('n2o-modal-actions', {
                            'n2o-disabled': disabled,
                        })}
                    >
                        <Toolbar toolbar={toolbar.bottomLeft} entityKey={entityKey} />
                        <Toolbar toolbar={toolbar.bottomCenter} entityKey={entityKey} />
                        <Toolbar toolbar={toolbar.bottomRight} entityKey={entityKey} />
                    </div>
                </ModalFooter>
            )}
        </Modal>
    )
}

export const ModalWindow = ModalPage

ModalPage.defaultProps = {
    size: 'lg',
    disabled: false,
    hasHeader: false,
    backdrop: 'static',
}

ModalPage.contextTypes = {
    resolveProps: PropTypes.func,
    scrollable: false,
}

// @ts-ignore import from js file
export default compose(withOverlayMethods)(ModalPage)
