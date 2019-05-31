import React from 'react';
import { storiesOf } from '@storybook/react';
import withPage from 'N2oStorybook/decorators/withPage';
import RangeField from 'N2oStorybook/json/RangeField';
import meta from './RangeField.meta';
import Factory from '../../../../../core/factory/Factory';
import { WIDGETS } from '../../../../../core/factory/factoryLevels';
console.log('point', RangeField);
const stories = storiesOf('Виджеты/Форма/Fields/RangeField');
const renderForm = json => (
  <Factory level={WIDGETS} {...json['Page_Form']} id="Page_Form" />
);
stories.add('Метаданные', () =>
  withPage(RangeField)(() => {
    return renderForm(RangeField);
  })
);
