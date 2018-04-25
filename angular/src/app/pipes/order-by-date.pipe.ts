import { Pipe, PipeTransform } from '@angular/core';
import { Annonce } from '../model/annonce/annonce';

@Pipe({
  name: 'orderAnnonceByDate'
})
export class OrderAnnonceByDatePipe implements PipeTransform {

  transform(iterator: IterableIterator<Annonce>, args?: any): Array<Annonce> {
    if (!iterator || iterator === undefined) return null;

    const array = Array.from(iterator);
    array.sort(((a: Annonce, b: Annonce) => {
      return a.created.getTime() - b.created.getTime();
    }));

    return array;
  }

}
