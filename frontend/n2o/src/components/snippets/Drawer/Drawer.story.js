import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs } from '@storybook/addon-knobs/react';
import { text, select } from '@storybook/addon-knobs';
import Drawer from './Drawer';
import { withState } from '@dump247/storybook-state';
import { Button } from 'reactstrap';

const stories = storiesOf('UI Компоненты/Drawer', module);

stories.addDecorator(withKnobs);

stories.add(
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
          title={<h2>title</h2>}
          footer="footer"
          {...drawerProps}
          visible={store.state.visible}
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
            style={{ zIndex: 10000 }}
          >
            {store.state.visible ? 'Close' : 'Open'}
          </Button>
        </div>
      </React.Fragment>
    );
  })
);
