import React from 'react';
import { forceReRender, storiesOf } from '@storybook/react';
import Drawer from './Drawer';
import { StateDecorator, Store } from '@sambego/storybook-state';
import { Button } from 'reactstrap';

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

stories.add(
  'Компонент',
  () => (
    <React.Fragment>
      <Drawer
        title={<h3>Заголовок</h3>}
        footer="Футер"
        visible={store.get('visible')}
        onClose={() => store.set({ visible: !store.get('visible') })}
        onHandleClick={() => store.set({ visible: !store.get('visible') })}
        width={'10%'}
        height={'100%'}
      >
        <p>Контент...</p>
        <p>Контент...</p>
        <p>Контент...</p>
      </Drawer>
      <div className="d-flex justify-content-center">
        <Button onClick={() => store.set({ visible: !store.get('visible') })}>
          {store.get('visible') ? 'Закрыть' : 'Открыть'}
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
      animation={true}
      closable={true},
      backdropClosable={false},
      visible={true},
      backdrop={true},
      backdropClosable={false}
      width={'10%'},
      height={'100%'}
    />
    ~~~
    `,
    },
  }
);
