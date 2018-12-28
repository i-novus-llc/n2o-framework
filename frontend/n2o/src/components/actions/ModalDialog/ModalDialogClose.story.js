import React from 'react';
import { storiesOf } from '@storybook/react';
import { withState } from '@dump247/storybook-state';

import ModalDialog from './ModalDialog';

const stories = storiesOf('UI Компоненты/Диалог', module);

stories.addWithJSX(
  'Кнопка закрытия',
  withState(
    {
      visible: false,
      closeButton: true
    },
    store => {
      return (
        <React.Fragment>
          <div className="btn-group">
            <button
              className="btn btn-secondary"
              onClick={() =>
                store.set({
                  visible: true,
                  closeButton: true
                })
              }
            >
              С кнопкой закрытия
            </button>
            <button
              className="btn btn-secondary"
              onClick={() =>
                store.set({
                  visible: true,
                  closeButton: false
                })
              }
            >
              Без кнопки закрытия
            </button>
          </div>
          <ModalDialog
            closeButton={store.state.closeButton}
            visible={store.state.visible}
            onDeny={() => store.set({ visible: false })}
            onConfirm={() => store.set({ visible: false })}
            close={() => store.set({ visible: false })}
          />
        </React.Fragment>
      );
    }
  )
);
