import React from 'react';
import { forceReRender, storiesOf } from '@storybook/react';
import Drawer from './Drawer';
import { StateDecorator, Store } from '@sambego/storybook-state';
import { Button } from 'reactstrap';
import MetaJSON from './Drawer.meta';

const stories = storiesOf('UI Компоненты/Drawer', module);

const store = new Store({
  visible: false,
});

store.subscribe(forceReRender);

stories.addDecorator(StateDecorator(store));

stories.addParameters({
  info: {
    propTables: [Drawer],
  },
});

stories
  .add(
    'Компонент',
    () => (
      <React.Fragment>
        <Drawer
          title={<h3>Title</h3>}
          footer="footer"
          visible={store.get('visible')}
          onClose={() => store.set({ visible: !store.get('visible') })}
          onHandleClick={() => store.set({ visible: !store.get('visible') })}
          backdropClosable={false}
        >
          <p>Some contents...</p>
          <p>Some contents...</p>
          <p>Some contents...</p>
        </Drawer>
        <div className="d-flex justify-content-center">
          <Button onClick={() => store.set({ visible: !store.get('visible') })}>
            {store.get('visible') ? 'Close' : 'Open'}
          </Button>
        </div>
      </React.Fragment>
    ),
    {
      info: {
        text: `
    Компонент 'Drawer'
    ~~~js
    import Drawer from 'n2o-framework/frontend/n2o-framework/lib/components/snippets/Drawer';
      
    <Drawer
      title="title"
      footer="footer"
      visible={true}
      onClose={onClose}
      onHandleClick={onHandleClick}
      backdropClosable={false}
    />
    ~~~
    `,
      },
    }
  )
  .add(
    'Метаданные',
    () => (
      <React.Fragment>
        <Drawer
          {...MetaJSON}
          visible={store.get('visible')}
          backdropClosable={false}
          onHandleClick={() => store.set({ visible: !store.get('visible') })}
          onClose={() => store.set({ visible: !store.get('visible') })}
        />
        <div className="d-flex justify-content-center">
          <Button onClick={() => store.set({ visible: !store.get('visible') })}>
            Open
          </Button>
        </div>
      </React.Fragment>
    ),
    {
      info: {
        text: `
    Компонент 'Drawer'
    ~~~js
    import Drawer from 'n2o-framework/frontend/n2o-framework/lib/components/snippets/Drawer';
      
    <Drawer
      {...props}
      visible={true}
      onClose={onClose}
      onHandleClick={onHandleClick}
      backdropClosable={false}
    />
    ~~~
    `,
      },
    }
  );
