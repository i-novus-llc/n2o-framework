import React from 'react'
import PropTypes from 'prop-types'
import { useTranslation } from 'react-i18next'
import { Button, ButtonGroup, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap'
import classNames from 'classnames'

/**
 * Диалог подтверждения действие
 * @reactProps {boolean} closeButton - крестик скрытия в углу.
 * @reactProps {string} size - размер (lg или sm)
 * @reactProps {string} title - текст заголовка диалога
 * @reactProps {string} text - основной текст
 * @reactProps {object} ok - конфиг кнопки подтверждения
 * @reactProps {string} ok.label - текст кнопки подтверждения
 * @reactProps {string} ok.color - цвет кнопки подтверждения
 * @reactProps {object} cancel - конфиг кнопки отмены
 * @reactProps {string} cancel.label - текст кнопки отмены
 * @reactProps {string} cancel.color - цвет кнопки отмены
 * @reactProps {boolean} visible - свойство видимости
 * @reactProps {boolean} reverseButtons - смена кнопок местами
 * @reactProps {function} onConfirm - вызывается при подтверждении
 * @reactProps {function} onDeny - вызывается при отмене
 * @reactProps {function} close
 * @example
 *  <ModalDialog {...button.confirm}
 *               visible={this.state.confirmVisibleId === button.id}
 *               onConfirm={() => {
 *                    this.onClickHelper(button);
 *                    this.closeConfirm();
 *                  }}
 *               onDeny={this.closeConfirm} />
 */
function ModalDialog({
    size,
    title,
    text,
    ok,
    cancel,
    onConfirm,
    onDeny,
    visible,
    close,
    closeButton,
    reverseButtons = false,
}) {
    const { t } = useTranslation()

    const { label: okLabel, color: okColor = 'primary' } = ok || {}
    const { label: cancelLabel, color: cancelColor } = cancel || {}

    return (
        <Modal
            isOpen={visible}
            size={size}
            toggle={close}
            modalClassName={classNames('n2o-modal-dialog', {
                'simple-modal-dialog': !title && !text,
            })}
        >
            {title && (
                <ModalHeader toggle={closeButton ? close : null}>
                    {title}
                </ModalHeader>
            )}
            {text && <ModalBody>{text}</ModalBody>}
            <ModalFooter>
                <ButtonGroup className={classNames({ 'flex-row-reverse': reverseButtons })}>
                    <Button onClick={onConfirm} color={okColor}>
                        {okLabel || t('confirm')}
                    </Button>
                    <Button onClick={onDeny} color={cancelColor}>{cancelLabel || t('deny')}</Button>
                </ButtonGroup>
            </ModalFooter>
        </Modal>
    )
}

ModalDialog.propTypes = {
    /**
     * Флаг показа кнопки закрытия в заголовке
     */
    closeButton: PropTypes.bool,
    /**
     * Размер окна
     */
    size: PropTypes.oneOf(['lg', 'sm']),
    /**
     * Заголовок окна
     */
    title: PropTypes.string,
    /**
     * Текст окна
     */
    text: PropTypes.string,
    /**
     * Конфиг кнопки отклонения
     */
    cancel: PropTypes.shape({
        label: PropTypes.string,
        color: PropTypes.string,
    }),
    /**
     * Конфиг кнопки подтверждения
     */
    ok: PropTypes.shape({
        label: PropTypes.string,
        color: PropTypes.string,
    }),
    /**
     * Видимость окна
     */
    visible: PropTypes.bool,
    /**
     * Callback подтверждения
     */
    onConfirm: PropTypes.func,
    /**
     * Callback отклонения
     */
    onDeny: PropTypes.func,
    /**
     * Функция закрытия окна
     */
    close: PropTypes.func.isRequired,
    /**
     * Смена кнопок местами
     */
    reverseButtons: PropTypes.bool,
}

export default ModalDialog
