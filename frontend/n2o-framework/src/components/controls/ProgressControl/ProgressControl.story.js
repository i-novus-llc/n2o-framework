import React from 'react';
import { storiesOf } from '@storybook/react';
import ProgressControl from './ProgressControl';
import ProgressControlMeta from './ProgressControl.meta';

const stories = storiesOf('Контролы/ProgressControl', module);

stories.add(
  'Компонент',
  () => {
    return <ProgressControl />;
  },
  {
    info: {
      text: `
     Компонент ProgressControl
      ~~~js
      import ProgressControl from 'n2o-framework/lib/components/snippets/ProgressControl/ProgressControl';
      
      <ProgressControl/>
      ~~~
      `,
    },
  }
);
stories.add(
  'Метаданные',
  () => {
    return <ProgressControl {...ProgressControlMeta['simple']} />;
  },
  {
    info: {
      text: `
     Компонент ProgressControl метаданные
      ~~~js
      import ProgressControl from 'n2o-framework/lib/components/snippets/ProgressControl/ProgressControl';
      
      <ProgressControl
        src=Progress
        barText=barText
        multi=false,
        value=50,
        max=100,
        animated=false,
        striped=false,
        color=success,
        className=progress-control-container,
        barClassName=progress-control
      />
      ~~~
      `,
    },
  }
);
stories.add(
  'Анимация',
  () => {
    return (
      <ProgressControl
        {...Object.assign({}, ProgressControlMeta['simple'], {
          animated: true,
        })}
      />
    );
  },
  {
    info: {
      text: `
     Компонент ProgressControl animated
      ~~~js
      import ProgressControl from 'n2o-framework/lib/components/snippets/ProgressControl/ProgressControl';
      
      <ProgressControl
        src=Progress
        barText=barText
        multi=false,
        value=50,
        max=100,
        animated=true,
        striped=false,
        color=success,
        className=progress-control-container,
        barClassName=progress-control
      />
      ~~~
      `,
    },
  }
);
stories.add(
  'Полоски без анимации',
  () => {
    return (
      <ProgressControl
        {...Object.assign({}, ProgressControlMeta['simple'], {
          striped: true,
        })}
      />
    );
  },
  {
    info: {
      text: `
     Компонент ProgressControl полоски
      ~~~js
      import ProgressControl from 'n2o-framework/lib/components/snippets/ProgressControl/ProgressControl';
      
      <ProgressControl
        src=Progress
        barText=barText
        multi=false,
        value=50,
        max=100,
        animated=false,
        striped=true,
        color=success,
        className=progress-control-container,
        barClassName=progress-control
      />
      ~~~
      `,
    },
  }
);
stories.add(
  'multi',
  () => {
    return <ProgressControl {...ProgressControlMeta['multi']} />;
  },
  {
    info: {
      text: `
     Компонент ProgressControl мультирежим
      ~~~js
      import ProgressControl from 'n2o-framework/lib/components/snippets/ProgressControl/ProgressControl';
      
      <ProgressControl
        src=Progress
        multi=true,
        max=100,
        className=progress-control-container,
        barClassName=progress-control",
        bars=[
          {
            "id": "progress1",
            "bar": true,
            "animated": true,
            "barText": "success 25%",
            "color": "success"
          },
          {
            "id": "progress2",
            "bar": true,
            "animated": false,
            "striped": true,
            "barText": "info 25%",
            "color": "info"
          }
          ,
          {
            "id": "progress3",
            "bar": true,
            "animated": false,
            "barText": "danger 25%",
            "color": "danger"
          }
          ,
          {
            "id": "progress4",
            "bar": true,
            "animated": false,
            "barText": "warning 25%",
            "color": "warning"
          }
        ]
        value={
            "progress1": 25,
            "progress2": 25,
            "progress3": 25,
            "progress4": 25
        }
      />
      ~~~
      `,
    },
  }
);
stories.add(
  'Максимальный прогресс 150',
  () => {
    return (
      <ProgressControl
        {...Object.assign({}, ProgressControlMeta['multi'], { max: 150 })}
      />
    );
  },
  {
    info: {
      text: `
     Компонент ProgressControl максимальный прогресс 150
      ~~~js
      import ProgressControl from 'n2o-framework/lib/components/snippets/ProgressControl/ProgressControl';
      
      <ProgressControl
        src=Progress
        multi=true,
        max=150,
        className=progress-control-container,
        barClassName=progress-control",
        bars=[
          {
            "id": "progress1",
            "bar": true,
            "animated": true,
            "barText": "success 25%",
            "color": "success"
          },
          {
            "id": "progress2",
            "bar": true,
            "animated": false,
            "striped": true,
            "barText": "info 25%",
            "color": "info"
          }
          ,
          {
            "id": "progress3",
            "bar": true,
            "animated": false,
            "barText": "danger 25%",
            "color": "danger"
          }
          ,
          {
            "id": "progress4",
            "bar": true,
            "animated": false,
            "barText": "warning 25%",
            "color": "warning"
          }
        ]
        value={
            "progress1": 25,
            "progress2": 25,
            "progress3": 25,
            "progress4": 25
        }
      />
      ~~~
      `,
    },
  }
);
