import React from 'react';
import PropTypes from 'prop-types';
import { Button, ButtonGroup, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { FormattedMessage } from 'react-intl';

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

class ModalDialog extends React.Component {
  /**
   * Рендер
   */

  render() {
    const {
      size,
      title,
      text,
      okLabel,
      cancelLabel,
      onConfirm,
      onDeny,
      visible,
      close,
      closeButton
    } = this.props;

    return (
      <Modal isOpen={visible} size={size} toggle={close}>
        <ModalHeader toggle={closeButton ? close : null}>
          {title || <FormattedMessage id="dialog.title" defaultMessage="Подтвердите действие" />}
        </ModalHeader>
        <ModalBody>
          {text || <FormattedMessage id="dialog.text" defaultMessage="Вы уверены?" />}
        </ModalBody>
        <ModalFooter>
          <ButtonGroup>
            <Button onClick={onConfirm} color="primary">
              {okLabel || <FormattedMessage id="dialog.confirm" defaultMessage="Да" />}
            </Button>
            <Button onClick={onDeny}>
              {cancelLabel || <FormattedMessage id="dialog.deny" defaultMessage="Нет" />}
            </Button>
          </ButtonGroup>
        </ModalFooter>
      </Modal>
    );
  }
}

ModalDialog.propTypes = {
  closeButton: PropTypes.bool,
  size: PropTypes.oneOf(['lg', 'sm']),
  title: PropTypes.string,
  text: PropTypes.string,
  okLabel: PropTypes.string,
  cancelLabel: PropTypes.string,
  visible: PropTypes.bool,
  onConfirm: PropTypes.func,
  onDeny: PropTypes.func,
  close: PropTypes.func.isRequired
};

export default ModalDialog;
