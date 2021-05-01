import React from 'react';
import { storiesOf, forceReRender } from '@storybook/react';

import { StateDecorator, Store } from '@sambego/storybook-state';

import ModalDialog from './ModalDialog';
import ModalDialogJson from './ModalDiaglog.meta.json';

const store = new Store({
  visible: false,
});

store.subscribe(() => {
  forceReRender();
});

const onShow = () => store.set({ visible: true });
const onHide = () => store.set({ visible: false });

const stories = storiesOf('UI Компоненты/Диалог', module);

stories.addDecorator(StateDecorator(store));

stories
  .add(
    'Компонент',
    () => {
      const props = {
        closeButton: true,
        size: 'lg',
        title: 'Подтвердите действие',
        text: 'Сохранить изменения',
        okLabel: 'Сохранить',
        cancelLabel: 'Отмена',
      };

      return (
        <React.Fragment>
          <button className="btn btn-secondary" onClick={onShow}>
            Показать диалог
          </button>
          <ModalDialog
            {...props}
            visible={store.get('visible')}
            onDeny={onHide}
            onConfirm={onHide}
          />
        </React.Fragment>
      );
    },
    {
      jsx: { skip: 1 },
      info: {
        text: `
        Компонент 'Модальное окно подтверждения'

        ~~~js
        import ModalDialog from 'n2o-framework/lib/components/actions/ModalDialog/ModalDialog';
        
        <ModalDialog 
            title="Подтвердите действие"
            text="Сохранить изменения"
            size="lg"
            closeButton={true}
            okLabel="Сохранить"
            cancelLabel="Отмена"
            visible={visible}
            onDeny={onDeny}
            onConfirm={onConfirm}
        />
        ~~~
        `,
      },
    }
  )

  .add(
    'Метаданные',
    () => {
      const props = {
        closeButton: ModalDialogJson.closeButton,
        size: ModalDialogJson.size,
        title: ModalDialogJson.title,
        text: ModalDialogJson.text,
        okLabel: ModalDialogJson.okLabel,
        cancelLabel: ModalDialogJson.cancelLabel,
      };

      return (
        <React.Fragment>
          <button className="btn btn-default" onClick={onShow}>
            Показать диалог
          </button>
          <ModalDialog
            {...props}
            visible={store.get('visible')}
            onDeny={onHide}
            onConfirm={onHide}
            close={onHide}
          />
        </React.Fragment>
      );
    },
    {
      info: {
        text: `
        Компонент 'Модальное окно подтверждения'

        ~~~js
        import ModalDialog from 'n2o-framework/lib/components/actions/ModalDialog/ModalDialog';
        
        <ModalDialog 
            title="Подтвердите действие"
            text="Сохранить изменения"
            size="lg"
            closeButton={true}
            okLabel="Сохранить"
            cancelLabel="Отмена"
            visible={visible}
            onDeny={onDeny}
            onConfirm={onConfirm}
        />
        ~~~
        `,
      },
    }
  );
