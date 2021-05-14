import React from 'react'
import PropTypes from 'prop-types'
import Modal from 'reactstrap/lib/Modal'
import ModalHeader from 'reactstrap/lib/ModalHeader'
import ModalBody from 'reactstrap/lib/ModalBody'
import ModalFooter from 'reactstrap/lib/ModalFooter'
import classNames from 'classnames'
import { compose } from 'recompose'

import Toolbar from '../buttons/Toolbar'
import { Spinner } from '../snippets/Spinner/Spinner'

// eslint-disable-next-line import/no-cycle,import/no-named-as-default
import Page from './Page'
import withOverlayMethods from './withOverlayMethods'
/**
 * Компонент, отображающий модальное окно
 * @reactProps {string} pageId - id пейджа
 * @reactProps {string} name - имя модалки
 * @reactProps {boolean} visible - отображается модалка или нет
 * @reactProps {string} size - размер('sm' или 'lg')
 * @reactProps {string | bool} backdrop -  наличие фона модального окна  false/true/'static'
 * @reactProps {string} headerTitle - заголовок в хэдере
 * @reactProps {boolean} closeButton - Есть кнопка закрытия или нет
 * @reactProps {object} actions - объект экшнов
 * @reactProps {array} toolbar - массив, описывающий внений вид кнопок-экшенов
 * @reactProps {object} props - аргументы для экшенов-функций
 * @reactProps {boolean}  disabled - блокировка модалки
 * @reactProps {function}  hidePrompt - скрытие окна подтверждения
 * @example
 *  <ModalPage props={props}
 *             actions={actions}
 *             name={name}
 *             pageId={pageId}
 *  />
 */
function ModalPage(props) {
    const {
        entityKey,
        toolbar,
        visible,
        headerTitle,
        loading,
        pageUrl,
        pageId,
        src,
        pathMapping,
        queryMapping,
        size,
        actions,
        close,
        disabled,
        scrollable,
        prompt,
        className,
        backdrop,
        style,
        hasHeader,
        ...rest
    } = props

    const pageMapping = {
        pathMapping,
        queryMapping,
    }

    const showSpinner = !visible || loading || typeof loading === 'undefined'
    const classes = classNames({ 'd-none': loading })

    return (
        <Spinner type="cover" loading={showSpinner} color="light" transparent>
            <Modal
                isOpen={visible}
                toggle={() => rest.closeOverlay(prompt)}
                size={size}
                style={style}
                scrollable={scrollable}
                className={className}
                backdrop={backdrop}
            >
                {hasHeader && (
                    <ModalHeader
                        className={classes}
                        toggle={() => rest.closeOverlay(prompt)}
                    >
                        {headerTitle}
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
                            needMetadata
                        />
                    ) : src ? (
                        rest.renderFromSrc(src)
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
        </Spinner>
    )
}

export const ModalWindow = ModalPage

ModalPage.propTypes = {
    /**
   * ID страницы
   */
    pageId: PropTypes.string,
    /**
   * Видимость модального окна
   */
    visible: PropTypes.bool,
    /**
   * Размер окна
   */
    size: PropTypes.oneOf(['lg', 'sm']),
    /**
   * Заголовок
   */
    headerTitle: PropTypes.string,
    /**
   * Включение кнопки закрытия
   */
    closeButton: PropTypes.bool,
    /**
   * Настройка кнопок
   */
    toolbar: PropTypes.array,
    /**
   * Название модалки
   */
    name: PropTypes.string,
    /**
   * Объект экшенов
   */
    actions: PropTypes.object,
    props: PropTypes.object,
    /**
   * Функция закрытия
   */
    close: PropTypes.func.isRequired,
    /**
   * Флаг активности
   */
    disabled: PropTypes.bool,
    /**
   * Класс модального окна
   */
    className: PropTypes.string,
    /**
   * Объект стилей
   */
    style: PropTypes.object,
    /**
   * Значение для отоборажения хедера
   */
    hasHeader: PropTypes.bool,
    /**
   * Фон модального окна
   */
    backdrop: PropTypes.oneOfType([PropTypes.bool, PropTypes.oneOf(['static'])]),
    entityKey: PropTypes.string,
    loading: PropTypes.bool,
    pageUrl: PropTypes.string,
    src: PropTypes.any,
    pathMapping: PropTypes.any,
    queryMapping: PropTypes.any,
    scrollable: PropTypes.bool,
    prompt: PropTypes.func,
}

ModalPage.defaultProps = {
    size: 'lg',
    headerTitle: 'Модальное окно',
    disabled: false,
    hasHeader: false,
    backdrop: 'static',
}

ModalPage.contextTypes = {
    resolveProps: PropTypes.func,
    scrollable: false,
}

export default compose(withOverlayMethods)(ModalPage)
