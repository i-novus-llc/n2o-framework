import React from 'react';
import { storiesOf } from '@storybook/react';

import ImageCell, { ImageCell as ImageCellComponent } from './ImageCell';
import imageShapes from './imageShapes';
import TextTableHeader from '../../headers/TextTableHeader';
import Table from '../../Table';
import Factory from '../../../../../core/factory/Factory';

const stories = storiesOf('Ячейки/Изображение', module);
stories.addParameters({
  info: {
    propTables: [ImageCellComponent],
    propTablesExclude: [Table, Factory],
  },
});

const IMAGEURL =
  'data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/PjxzdmcgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB3aWR0aD0iMTQwIiBoZWlnaHQ9IjE0MCIgdmlld0JveD0iMCAwIDE0MCAxNDAiIHByZXNlcnZlQXNwZWN0UmF0aW89Im5vbmUiPjwhLS0KU291cmNlIFVSTDogaG9sZGVyLmpzLzE0MHgxNDAKQ3JlYXRlZCB3aXRoIEhvbGRlci5qcyAyLjYuMC4KTGVhcm4gbW9yZSBhdCBodHRwOi8vaG9sZGVyanMuY29tCihjKSAyMDEyLTIwMTUgSXZhbiBNYWxvcGluc2t5IC0gaHR0cDovL2ltc2t5LmNvCi0tPjxkZWZzPjxzdHlsZSB0eXBlPSJ0ZXh0L2NzcyI+PCFbQ0RBVEFbI2hvbGRlcl8xNjJiNDE2OGJmYSB0ZXh0IHsgZmlsbDojQUFBQUFBO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1mYW1pbHk6QXJpYWwsIEhlbHZldGljYSwgT3BlbiBTYW5zLCBzYW5zLXNlcmlmLCBtb25vc3BhY2U7Zm9udC1zaXplOjEwcHQgfSBdXT48L3N0eWxlPjwvZGVmcz48ZyBpZD0iaG9sZGVyXzE2MmI0MTY4YmZhIj48cmVjdCB3aWR0aD0iMTQwIiBoZWlnaHQ9IjE0MCIgZmlsbD0iI0VFRUVFRSIvPjxnPjx0ZXh0IHg9IjQ0LjA1NDY4NzUiIHk9Ijc0LjUiPjE0MHgxNDA8L3RleHQ+PC9nPjwvZz48L3N2Zz4=';

stories.add('Компонент', () => {
  const tableProps = {
    headers: [
      {
        id: 'rounded',
        component: TextTableHeader,
        label: 'Закруглённый',
      },
      {
        id: 'circle',
        component: TextTableHeader,
        label: 'Круглый',
      },
      {
        id: 'thumbnail',
        component: TextTableHeader,
        label: 'С рамкой',
      },
    ],
    cells: [
      {
        id: 'rounded',
        component: ImageCell,
        shape: imageShapes.ROUNDED,
        title: 'Закруглённый вариант',
      },
      {
        id: 'circle',
        component: ImageCell,
        shape: imageShapes.CIRCLE,
        title: 'Круглый вариант',
      },
      {
        id: 'thumbnail',
        component: ImageCell,
        shape: imageShapes.THUMBNAIL,
        title: 'Вариант с рамкой',
      },
    ],
    datasource: [
      {
        id: 'data',
        rounded: IMAGEURL,
        circle: IMAGEURL,
        thumbnail: IMAGEURL,
      },
    ],
  };

  return (
    <Table
      headers={tableProps.headers}
      cells={tableProps.cells}
      datasource={tableProps.datasource}
    />
  );
});
