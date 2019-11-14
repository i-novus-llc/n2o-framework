import React from 'react'
import compose from 'recompose/compose';
import withState from 'recompose/withState';
import withHandlers from 'recompose/withHandlers';
import Modal from 'reactstrap/lib/Modal';
import ModalHeader from 'reactstrap/lib/ModalHeader';
import ModalBody from 'reactstrap/lib/ModalBody';
import ModalFooter from 'reactstrap/lib/ModalFooter';
import Button from 'reactstrap/lib/Button';
import InputSelect from 'n2o-framework/lib/components/controls/InputSelect/InputSelect';
import Alerts from 'n2o-framework/lib/components/snippets/Alerts/Alerts';

function EcpComponent({ toggle, isOpen, content = [
    { id: '1', name: 'Сертификат',},] }) {

    const alerts = [ {text: 'text', visible: false, position: 'absolute'},];

    const Alert = (alerts) => {
        const errorParams = { label: 'Ошибка!', severity: 'danger' };
        const successParams = { label: 'Успех', severity: 'success' };
        return alerts.map(alert => alert.error ? {...alert, ...errorParams} : {...alert, ...successParams})
    };

  return (
      <div className="n2o-ecp-plugin">
          <Button onClick={toggle}>Open ECP</Button>
          <Modal isOpen={isOpen}>
              <Alerts alerts={Alert(alerts)} />
              <ModalHeader>N2O ECP plugin</ModalHeader>
              <ModalBody>
                  <InputSelect options={content} />
              </ModalBody>
              <ModalFooter className='d-flex justify-content-between'>
                  <Button>Подписать</Button>
                  <Button onClick={toggle}>Отмена</Button>
              </ModalFooter>
          </Modal>
      </div>
  );
}

export default compose(
    withState('isOpen', 'setOpen', ({ isOpen }) => isOpen),
    withHandlers({
        toggle: ({ isOpen, setOpen }) => () => {
            setOpen(!isOpen);
        },
        onConfirm: ({ isOpen, stateUpdate, onConfirm }) => () => {},
        onDeny: ({ setOpen, isOpen }) => () => {},
    })
)(EcpComponent)
