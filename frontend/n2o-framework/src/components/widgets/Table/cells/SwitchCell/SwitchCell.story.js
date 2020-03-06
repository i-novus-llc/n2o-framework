import React from 'react';
import { storiesOf } from '@storybook/react';

import TextTableHeader from '../../headers/TextTableHeader';
import Table from '../../Table';
import Factory from '../../../../../core/factory/Factory';

import SwitchCell from './SwitchCell';
import withPage from 'N2oStorybook/decorators/withPage';
import set from 'lodash/set';
import fetchMock from 'fetch-mock';
import { CELLS, WIDGETS } from '../../../../../core/factory/factoryLevels';
import TextCell from '../TextCell/TextCell';
import LinkCell from '../LinkCell/LinkCell';
import LinkCellJson from '../LinkCell/LinkCell.meta';

const stories = storiesOf('Ячейки/Переключаемая ячейка', module);

const linkCellProps = {
  id: 'name',
  src: 'LinkCell',
  fieldKey: 'name',
  className: 'n2o',
  label: 'link',
  style: {},
  icon: 'icon',
  type: 'text',
  url: '/page/widget/test',
  target: 'application',
};

const model = {
  src: 'SwitchCell',
  switchFieldId: 'LinkCell', // поле в модели, в котором лежит ключ нужного cell
  switchList: {
    // список возможных ячеек
    TextCell: {
      src: 'TextCell',
      fieldKey: 'text',
      text: 'test text',
    },
    LinkCell: {
      ...linkCellProps,
    },
  },
  switchDefault: {},
};
stories.add(
  'Компонент',
  () => {
    return <SwitchCell model={model} />;
  },
  {
    info: {
      text: `
      ~~~js
      import ListTextCell from 'n2o-framework/lib/components/widgets/Table/cells/ListTextCell/ListTextCell';
      
      <ListTextCell
        id="id компонента"
        src="src копонента"
        label="{value} объектов, текст с плейсхолдером. {value} - длина массива. Если {value} нет, то длина вставиться перед словом"
        fieldKey="Ключ по которому берутся данные из model"
        trigger="hover или click, default=hover"
        labelDashed="флаг underline dashed для label, default=false"
        placement="позиция tooltip (bottom, right, top, left) default=bottom"
        theme="light или dark(default)"
       />
      ~~~
      `,
    },
  }
);
