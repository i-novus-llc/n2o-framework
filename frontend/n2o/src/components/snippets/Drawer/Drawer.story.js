import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs } from '@storybook/addon-knobs/react';
import { text, select } from '@storybook/addon-knobs';
import Drawer from './Drawer';
import { withState } from '@dump247/storybook-state';
import { Button } from 'reactstrap';
import MetaJSON from './Drawer.meta';

const stories = storiesOf('UI Компоненты/Drawer', module);

stories.addDecorator(withKnobs);

stories
  .add(
    'Компонент',
    withState({ visible: false }, store => {
      const drawerProps = {
        placement: select('placement', ['left', 'right', 'top', 'bottom']),
        width: text('width', ''),
        height: text('height', ''),
      };

      return (
        <React.Fragment>
          <Drawer
            title="title"
            footer="footer"
            {...drawerProps}
            visible={store.state.visible}
            onClose={() => {
              store.set({ visible: !store.state.visible });
            }}
          >
            <p>Some contents...</p>
            <p>Some contents...</p>
            <p>Some contents...</p>
          </Drawer>
          <div className="d-flex justify-content-center">
            <Button
              onClick={() => {
                store.set({ visible: !store.state.visible });
              }}
            >
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
      return (
        <React.Fragment>
          <Drawer
            {...MetaJSON}
            visible={store.state.visible}
            onClose={() => {
              store.set({ visible: !store.state.visible });
            }}
          />
          <div className="d-flex justify-content-center">
            <Button
              onClick={() => {
                store.set({ visible: !store.state.visible });
              }}
            >
              {store.state.visible ? 'Close' : 'Open'}
            </Button>
          </div>
        </React.Fragment>
      );
    })
  );
