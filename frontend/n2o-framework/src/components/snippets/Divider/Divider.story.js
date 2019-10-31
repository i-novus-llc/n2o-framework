import React from 'react';
import { storiesOf } from '@storybook/react';
import { Divider } from './Divider';

const stories = storiesOf('UI Компоненты/Divider', module);

const DividerToStory = props => {
  return <Divider {...props} />;
};

stories.add('Компонент', () => {
  return <DividerToStory />;
});

stories.add('Горизонтальный', () => {
  return <DividerToStory type={'horizontal'} />;
});

stories.add('Пунктирная линия', () => {
  return <DividerToStory dashed={true} />;
});

stories.add('С заголовком (по умолчанию лево)', () => {
  return <DividerToStory children={'Divider title'} />;
});

stories.add('Пунктирна линия, с заголовком (по умолчанию лево)', () => {
  return <DividerToStory dashed={true} children={'Divider title'} />;
});

stories.add('С заголовком (центр)', () => {
  return <DividerToStory position={'center'} children={'Divider title'} />;
});

stories.add('С заголовком (право)', () => {
  return <DividerToStory position={'right'} children={'Divider title'} />;
});

stories.add('Вертикальный, высота 250px', () => {
  return <DividerToStory type={'vertical'} style={{ height: '250px' }} />;
});

stories.add('Вертикальный, пунктирная линия, высота 250px', () => {
  return (
    <DividerToStory
      dashed={true}
      type={'vertical'}
      style={{ height: '250px' }}
    />
  );
});

stories.add('Стилизация цвета заголовка', () => {
  return (
    <section>
      <div style={{ margin: '30px' }}>
        <DividerToStory
          style={{ width: '100%', color: '#b4004e' }}
          children={'Divider title'}
        />
      </div>
      <div style={{ margin: '30px' }}>
        <DividerToStory
          style={{ width: '100%', color: '#00766c' }}
          children={'Divider title'}
          position={'center'}
        />
      </div>
      <div style={{ margin: '30px' }}>
        <DividerToStory
          style={{ width: '100%', color: '#8eacbb' }}
          children={'Divider title'}
          position={'right'}
        />
      </div>
    </section>
  );
});

stories.add('Различная ширина (style)', () => {
  return (
    <section>
      <div style={{ margin: '30px' }}>
        <DividerToStory style={{ width: '25%' }} children={'25%'} />
      </div>
      <div style={{ margin: '30px' }}>
        <DividerToStory style={{ width: '50%' }} children={'50%'} />
      </div>
      <div style={{ margin: '30px' }}>
        <DividerToStory style={{ width: '75%' }} children={'75%'} />
      </div>
      <div style={{ margin: '30px' }}>
        <DividerToStory style={{ width: '100%' }} children={'100%'} />
      </div>
    </section>
  );
});
