import React from 'react';
import TitleFieldset from './TitleFieldset';
import { storiesOf } from '@storybook/react';
import meta from './TitleFieldset.meta.json';

const stories = storiesOf('UI Компоненты/TitleFieldset', module);

stories
  .add('Компонент', () => {
    return <TitleFieldset render={() => {}} title={'Заголовок'} showLine={true} />;
  })
  .add('Метаданные', () => {
    return <TitleFieldset render={() => {}} {...meta} />;
  })
  .add('Заголовок', () => {
    return <TitleFieldset render={() => {}} title={'Заголовок'} />;
  })
  .add('Заголовок и подзаголовок', () => {
    return <TitleFieldset render={() => {}} title={'Заголовок'} subTitle={'Подзаголовок'} />;
  })
  .add('Без заголовков', () => {
    return <TitleFieldset render={() => {}} showLine={true} />;
  });
