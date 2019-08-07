import React from 'react';
import { pick } from 'lodash';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { WIDGETS } from '../factory/factoryLevels';
import FieldDependency from './FieldDependency.meta';
import { parseUrl } from 'N2oStorybook/fetchMock';
import Factory from '../factory/Factory';

const stories = storiesOf('Функциональность/Зависимость между полями', module);

stories.addDecorator(jsxDecorator).add('Метаданные', () => {
  return (
    <Factory
      level={WIDGETS}
      id={'Page_Form'}
      {...FieldDependency['Page_Form']}
    />
  );
});
