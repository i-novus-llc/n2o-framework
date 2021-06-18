import React from 'react'
import PropTypes from 'prop-types'
import { useTranslation } from 'react-i18next'
import Button from 'reactstrap/lib/Button'
import ButtonGroup from 'reactstrap/lib/ButtonGroup'
import Modal from 'reactstrap/lib/Modal'
import ModalHeader from 'reactstrap/lib/ModalHeader'
import ModalBody from 'reactstrap/lib/ModalBody'
import ModalFooter from 'reactstrap/lib/ModalFooter'

/**
 * Диалог подтверждения действие
 * @reactProps {boolean} closeButton - крестик скрытия в углу.
 * @reactProps {string} size - размер (lg или sm)
 * @reactProps {string} title - текст заголовка диалога
 * @reactProps {string} text - основной текст
 * @reactProps {string} okLabel - текст на кнопке подтверждения
 * @reactProps {string} cancelLabel - текст на кнопке отмены
 * @reactProps {boolean} visible - свойство видимости
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
    okLabel,
    cancelLabel,
    onConfirm,
    onDeny,
    visible,
    close,
    closeButton,
}) {
    const { t } = useTranslation()

    return (
        <Modal isOpen={visible} size={size} toggle={close}>
            <ModalHeader toggle={closeButton ? close : null}>
                {title || t('dialogTitle')}
            </ModalHeader>
            <ModalBody>{text || t('dialogText')}</ModalBody>
            <ModalFooter>
                <ButtonGroup>
                    <Button onClick={onConfirm} color="primary">
                        {okLabel || t('confirm')}
                    </Button>
                    <Button onClick={onDeny}>{cancelLabel || t('deny')}</Button>
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
   * Текст кнопки отклонения
   */
    cancelLabel: PropTypes.string,
    /**
   * Текст кнопки подтверждения
   */
    okLabel: PropTypes.string,
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
}

export default ModalDialog
