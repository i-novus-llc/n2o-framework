import React from 'react';
import { Jumbotron, Button } from "reactstrap";
import Factory from  "n2o-framework/lib/core/factory/Factory";
import { WIDGETS } from "n2o-framework/lib/core/factory/factoryLevels";

function JumbotronPage(props) {
  const { regions } = props;
  return (
    <div className={'custom-page'}>
      <Jumbotron>
        <h1 className="display-3">Привет, мир!</h1>
        <p className="lead">Простой пример кастомной страницы N2O</p>
        <hr className="my-2" />
        {
          regions.single.map(region => {
            return (<Factory
                key={`page-${region.id}`}
                level={WIDGETS}
                {...region}
                pageId={region.id}
            />)
          })
        }
        <p>Узнать больше можно в репозитории GitHub</p>
        <p className="lead">
          <Button tag='a' color="primary" href='https://github.com/i-novus-llc/n2o-framework' target='_blank'>GitHub</Button>
        </p>
      </Jumbotron>
    </div>
  );
}

export default JumbotronPage;
