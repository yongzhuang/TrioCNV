package cn.edu.hit.triocnv.file;


import cn.edu.hit.triocnv.file.ComparableSamRecordIterator;
import java.util.Collection;
import java.util.PriorityQueue;
import net.sf.picard.sam.MergingSamRecordIterator;
import net.sf.samtools.SAMFileHeader;
import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import net.sf.samtools.SAMRecordComparator;
import net.sf.samtools.SAMRecordCoordinateComparator;
import net.sf.samtools.util.CloseableIterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Yongzhuang Liu
 */
public class MultiSamRecordIterator {

    private final PriorityQueue<ComparableSamRecordIterator> pq;

    public MultiSamRecordIterator(Collection<SAMFileReader> readers) {
        this.pq = new PriorityQueue<ComparableSamRecordIterator>(readers.size());
        for (SAMFileReader reader : readers) {
            addIfNotEmpty(new ComparableSamRecordIterator(reader, reader.iterator(), new SAMRecordCoordinateComparator()));
        }
    }

    private void addIfNotEmpty(final ComparableSamRecordIterator iterator) {
        if (iterator.hasNext()) {
            pq.offer(iterator);
        } else {
            iterator.close();
        }
    }

    public boolean hasNext() {
        return !this.pq.isEmpty();
    }

    public SAMRecord next() {
        ComparableSamRecordIterator iterator = this.pq.poll();
        SAMRecord record = iterator.next();
        addIfNotEmpty(iterator);
        return record;
    }

    public void close() {
        for (CloseableIterator<SAMRecord> iterator : pq) {
            iterator.close();
        }
    }
}
