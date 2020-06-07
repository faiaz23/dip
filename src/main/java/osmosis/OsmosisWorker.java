package osmosis;

import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.pbf2.v0_6.PbfReader;

import java.nio.file.Path;

public class OsmosisWorker {
    private PbfReader reader;

    public void readPbf(Path pbfFile) {
        reader = new PbfReader(pbfFile.toFile(), 1);
    }

    public void setSink(Sink sink) {
        reader.setSink(sink);
    }

    public void process() {
        reader.run();
    }
}
