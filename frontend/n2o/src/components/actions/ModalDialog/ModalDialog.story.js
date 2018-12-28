import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, select } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import { withState } from '@dump247/storybook-state';

import ModalDialog from './ModalDialog';
import ModalDialogJson from './ModalDiaglog.meta.json';

const stories = storiesOf('UI Компоненты/Диалог', module);

stories.addDecorator(withKnobs);
// todo: баг в jest addon
stories.addDecorator(withTests('ModalDialog'));

stories
  .addWithJSX(
    'Компонент',
    withState({ visible: false }, store => {
      const props = {
        closeButton: boolean('closeButton', true),
        size: select('size', ['lg', 'sm'], 'lg'),
        title: text('title', 'Подтвердите действие'),
        text: text('text', 'Сохранить изменения'),
        confirmText: text('confirmText', 'Сохранить'),
        denyText: text('denyText', 'Отмена')
      };

      return (
        <React.Fragment>
          <button className="btn btn-secondary" onClick={() => store.set({ visible: true })}>
            Показать диалог
          </button>
          <ModalDialog
            {...props}
            visible={store.state.visible}
            onDeny={() => store.set({ visible: false })}
            onConfirm={() => store.set({ visible: false })}
          />
        </React.Fragment>
      );
    })
  )

  .add(
    'Метаданные',
    withState({ visible: ModalDialogJson.visible }, store => {
      const props = {
        closeButton: boolean('closeButton', ModalDialogJson.closeButton),
        size: select('size', ['lg', 'sm'], ModalDialogJson.size),
        title: text('title', ModalDialogJson.title),
        text: text('text', ModalDialogJson.text),
        confirmText: text('confirmText', ModalDialogJson.confirmText),
        denyText: text('denyText', ModalDialogJson.denyText)
      };

      return (
        <React.Fragment>
          <button className="btn btn-default" onClick={() => store.set({ visible: true })}>
            Показать диалог
          </button>
          <ModalDialog
            {...props}
            visible={store.state.visible}
            onDeny={() => store.set({ visible: false })}
            onConfirm={() => store.set({ visible: false })}
            close={() => store.set({ visible: false })}
          />
        </React.Fragment>
      );
    })
  );
