import React from 'react';
import { storiesOf } from '@storybook/react';
import { withState } from '@dump247/storybook-state';

import ModalDialog from './ModalDialog';

const stories = storiesOf('UI Компоненты/Диалог', module);

stories.add(
  'Размеры',
  withState(
    {
      visible: false,
      size: 'sm',
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
                  size: 'sm',
                })
              }
            >
              Показать маленький
            </button>
            <button
              className="btn btn-secondary"
              onClick={() =>
                store.set({
                  visible: true,
                  size: 'lg',
                })
              }
            >
              Показать большой
            </button>
          </div>
          <ModalDialog
            size={store.state.size}
            visible={store.state.visible}
            onDeny={() => store.set({ visible: false })}
            onConfirm={() => store.set({ visible: false })}
          />
        </React.Fragment>
      );
    }
  )
);
