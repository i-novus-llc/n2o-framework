import React from 'react';
import { storiesOf } from '@storybook/react';
import Drawer from './Drawer';
import { withState } from '@dump247/storybook-state';
import { Button } from 'reactstrap';
import MetaJSON from './Drawer.meta';

const stories = storiesOf('UI Компоненты/Drawer', module);

stories
  .add(
    'Компонент',
    withState({ visible: false }, store => {
      const close = () => {
        store.set({ visible: !store.state.visible });
      };

      return (
        <React.Fragment>
          <Drawer
            title={<h3>Title</h3>}
            footer="footer"
            visible={store.state.visible}
            onClose={close}
            onHandleClick={close}
            backdropClosable={false}
          >
            <p>Some contents...</p>
            <p>Some contents...</p>
            <p>Some contents...</p>
          </Drawer>
          <div className="d-flex justify-content-center">
            <Button onClick={close}>
              {store.state.visible ? 'Close' : 'Open'}
            </Button>
          </div>
        </React.Fragment>
      );
    })
  )
  .add(
    'Метаданные',
    withState({ visible: false }, store => {
      const close = () => {
        store.set({ visible: !store.state.visible });
      };

      return (
        <React.Fragment>
          <Drawer
            {...MetaJSON}
            visible={store.state.visible}
            backdropClosable={false}
            onHandleClick={close}
            onClose={close}
          />
          <div className="d-flex justify-content-center">
            <Button onClick={close}>Open</Button>
          </div>
        </React.Fragment>
      );
    })
  );
