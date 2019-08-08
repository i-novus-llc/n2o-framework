import React from 'react';
import { storiesOf, forceReRender } from '@storybook/react';
import { withKnobs, text, boolean, select } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
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

stories.addDecorator(withKnobs);
// todo: баг в jest addon
stories.addDecorator(withTests('ModalDialog'));
stories.addDecorator(StateDecorator(store));

stories
  .addWithJSX(
    'Компонент',
    () => {
      const props = {
        closeButton: boolean('closeButton', true),
        size: select('size', ['lg', 'sm'], 'lg'),
        title: text('title', 'Подтвердите действие'),
        text: text('text', 'Сохранить изменения'),
        confirmText: text('confirmText', 'Сохранить'),
        denyText: text('denyText', 'Отмена'),
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
    { jsx: { skip: 1 } }
  )

  .addWithJSX('Метаданные', () => {
    const props = {
      closeButton: boolean('closeButton', ModalDialogJson.closeButton),
      size: select('size', ['lg', 'sm'], ModalDialogJson.size),
      title: text('title', ModalDialogJson.title),
      text: text('text', ModalDialogJson.text),
      confirmText: text('confirmText', ModalDialogJson.confirmText),
      denyText: text('denyText', ModalDialogJson.denyText),
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
  });
