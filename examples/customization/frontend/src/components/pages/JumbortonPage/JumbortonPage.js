import React from 'react';
import { Jumbotron, Button } from "reactstrap";

function JumbortonPage(props) {
  return (
    <div className={'custom-page'}>
      <Jumbotron>
        <h1 className="display-3">Привет, мир!</h1>
        <p className="lead">Простой пример кастомной страницы N2O</p>
        <hr className="my-2" />
        <p>Узнать больше можно в репозитории GitHub</p>
        <p className="lead">
          <Button tag='a' color="primary" href='https://github.com/i-novus-llc/n2o-framework' target='_blank'>GitHub</Button>
        </p>
      </Jumbotron>
    </div>
  );
}

export default JumbortonPage;
