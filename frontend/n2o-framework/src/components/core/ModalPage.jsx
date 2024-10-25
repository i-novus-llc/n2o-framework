import React from 'react'
import PropTypes from 'prop-types'
import { Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap'
import classNames from 'classnames'
import { compose } from 'recompose'

import Toolbar from '../buttons/Toolbar'

import Page from './Page'
import { PageTitle } from './PageTitle'
import withOverlayMethods from './withOverlayMethods'
/**
 * Компонент, отображающий модальное окно
 * @reactProps {string} pageId - id пейджа
 * @reactProps {string} name - имя модалки
 * @reactProps {boolean} visible - отображается модалка или нет
 * @reactProps {string} size - размер('sm' или 'lg')
 * @reactProps {string | bool} backdrop -  наличие фона модального окна  false/true/'static'
 * @reactProps {string} modalHeaderTitle - заголовок в хэдере
 * @reactProps {boolean} closeButton - Есть кнопка закрытия или нет
 * @reactProps {array} toolbar - массив, описывающий внений вид кнопок-экшенов
 * @reactProps {object} props - аргументы для экшенов-функций
 * @reactProps {boolean}  disabled - блокировка модалки
 * @reactProps {function}  hidePrompt - скрытие окна подтверждения
 * @example
 *  <ModalPage props={props}
 *             name={name}
 *             pageId={pageId}
 *  />
 */

function ModalPage({
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
    ...modalProps
}) {
    const { page = {} } = metadata
    const { modalHeaderTitle, datasource, model: modelPrefix = 'resolve' } = page

    const pageMapping = { pathMapping, queryMapping }

    const classes = classNames({ 'd-none': loading })

    return (
        <Modal
            {...modalProps}
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
                {/* eslint-disable-next-line no-nested-ternary */}
                {pageUrl ? (
                    <Page
                        pageUrl={pageUrl}
                        pageId={pageId}
                        pageMapping={pageMapping}
                        entityKey={entityKey}
                        routable={false}
                        needMetadata
                    />
                ) : src ? (
                    renderFromSrc(src)
                ) : null}
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

export default compose(withOverlayMethods)(ModalPage)
