import React from 'react';
import { storiesOf } from '@storybook/react';
import fetchMock from 'fetch-mock';

import metadata from './TilesWidget.meta.json';
import { Tiles } from './Tiles';
import TilesWidget from './TilesWidget';

const stories = storiesOf('Виджеты/Виджет Карточки', module);

stories.addParameters({
  info: {
    propTables: [Tiles],
    propTablesExclude: [TilesWidget],
  },
});

const urlPattern = '*';

stories.add('Компонент', () => {
  fetchMock.restore().get(urlPattern, url => {
    return {
      meta: {},
      page: 0,
      size: 10,
      list: [
        {
          tile1:
            'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTivmPCOe7GabP3hXYjSilJL1rkyrKF65PLV-oFw4MsOagkurzQ',
          tile2: 'Карточка 1',
        },
        {
          tile1:
            'https://upload.wikimedia.org/wikipedia/commons/2/2e/Not_boyfriends_computer.jpg',
          tile2: 'Карточка 2',
        },
        {
          tile1:
            'https://www.zastavki.com/pictures/1024x1024/2015/Girls_Smiling_beautiful_girl__photo_George_Chernyad_ev_111193_31.jpg',
          tile2: 'Карточка 3',
        },
        {
          tile1:
            'http://sedayejavedan.persiangig.com/blue,girl,lonely,sad-a470b418368e548210d276093519d9ad_h.jpg',
          tile2: 'Карточка 4',
        },
      ],
    };
  });

  const withDataProvider = {
    dataProvider: {
      url: 'n2o/data/test',
      pathMapping: {},
      queryMapping: {
        'filter.name': {
          link: "models.filter['Page_Table'].name",
        },
        'filter.surname': {
          link: "models.filter['Page_Table'].surname",
        },
      },
    },
    paging: {
      size: 10,
    },
    ...metadata['Page_Tiles'],
  };

  return <TilesWidget {...withDataProvider} id="Page_Tiles" />;
});
